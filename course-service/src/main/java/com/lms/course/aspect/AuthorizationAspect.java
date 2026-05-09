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
    
    private final java.util.List<com.lms.course.security.strategy.AuthorizationStrategy> strategies;
    
    @Before("@annotation(requireCourseOwner)")
    public void checkCourseOwner(JoinPoint joinPoint, com.lms.course.annotation.RequireCourseOwner requireCourseOwner) {
        executeStrategy(joinPoint, requireCourseOwner);
    }

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, com.lms.course.annotation.RequireRole requireRole) {
        executeStrategy(joinPoint, requireRole);
    }

    private void executeStrategy(JoinPoint joinPoint, Object annotation) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        Class<?> annotationType = annotation.getClass();
        if (annotation instanceof java.lang.annotation.Annotation) {
            annotationType = ((java.lang.annotation.Annotation) annotation).annotationType();
        }

        final Class<?> finalType = annotationType;
        strategies.stream()
                .filter(s -> s.supports(finalType))
                .findFirst()
                .ifPresentOrElse(
                        s -> s.authorize(joinPoint, request, annotation),
                        () -> { throw new RuntimeException("No authorization strategy found for " + finalType.getSimpleName()); }
                );
    }
}
