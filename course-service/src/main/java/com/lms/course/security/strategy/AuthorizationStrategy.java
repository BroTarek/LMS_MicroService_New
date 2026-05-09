package com.lms.course.security.strategy;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;

public interface AuthorizationStrategy {
    /**
     * Executes the authorization check.
     * @param joinPoint The join point of the intercepted method.
     * @param request The current HTTP request.
     * @param annotation The annotation instance containing metadata.
     * @throws RuntimeException if authorization fails.
     */
    void authorize(JoinPoint joinPoint, HttpServletRequest request, Object annotation);
    
    /**
     * Determines if this strategy can handle the given annotation type.
     */
    boolean supports(Class<?> annotationType);
}
