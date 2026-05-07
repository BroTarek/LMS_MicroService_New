package com.lms.course.aspect;

import com.lms.course.annotation.RequireCourseOwner;
import com.lms.course.annotation.RequireRole;
import com.lms.course.entity.Course;
import com.lms.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {
    
    private final CourseRepository courseRepository;
    
    @Before("@annotation(requireCourseOwner)")
    public void checkCourseOwner(JoinPoint joinPoint, RequireCourseOwner requireCourseOwner) {
        // Extract courseId from method arguments
        Object[] args = joinPoint.getArgs();
        Long courseId = null;
        for (Object arg : args) {
            if (arg instanceof Long) {
                courseId = (Long) arg;
                break;
            }
        }
        if (courseId == null) {
            throw new RuntimeException("Course ID not found in method arguments");
        }
        
        // Get user info from request headers
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String username = request.getHeader("X-Username");
        String role = request.getHeader("X-Role");

        if (username == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Admin override
        if ("ADMIN".equals(role)) {
            return;
        }
        
        final Long finalCourseId = courseId;
        Course course = courseRepository.findById(finalCourseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + finalCourseId));
        if (!course.getTeacherUsername().equals(username)) {
            throw new RuntimeException("You are not the owner of this course");
        }
    }

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userRole = request.getHeader("X-Role");
        if (userRole == null) {
            throw new RuntimeException("User role not found in headers");
        }
        
        String[] allowedRoles = requireRole.value();
        boolean authorized = java.util.Arrays.asList(allowedRoles).contains(userRole);
        
        if (!authorized) {
            throw new RuntimeException("Access denied. Required roles: " +
                    String.join(" or ", allowedRoles) +
                    ". Your role: " + userRole);
        }
    }
}
