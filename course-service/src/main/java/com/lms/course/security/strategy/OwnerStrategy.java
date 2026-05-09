package com.lms.course.security.strategy;

import com.lms.course.annotation.RequireCourseOwner;
import com.lms.course.entity.Course;
import com.lms.course.repository.CourseRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerStrategy implements AuthorizationStrategy {

    private final CourseRepository courseRepository;

    @Override
    public void authorize(JoinPoint joinPoint, HttpServletRequest request, Object annotation) {
        String username = request.getHeader("X-Username");
        String role = request.getHeader("X-Role");

        if (username == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Admin override logic encapsulated here
        if ("ADMIN".equals(role)) {
            return;
        }

        Long courseId = extractCourseId(joinPoint);
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));
        
        if (!course.getTeacherUsername().equals(username)) {
            throw new RuntimeException("You are not the owner of this course");
        }
    }

    private Long extractCourseId(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            }
        }
        throw new RuntimeException("Course ID not found in method arguments");
    }

    @Override
    public boolean supports(Class<?> annotationType) {
        return RequireCourseOwner.class.equals(annotationType);
    }
}
