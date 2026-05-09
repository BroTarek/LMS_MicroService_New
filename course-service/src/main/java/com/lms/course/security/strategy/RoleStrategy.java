package com.lms.course.security.strategy;

import com.lms.course.annotation.RequireRole;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class RoleStrategy implements AuthorizationStrategy {

    @Override
    public void authorize(JoinPoint joinPoint, HttpServletRequest request, Object annotation) {
        RequireRole requireRole = (RequireRole) annotation;
        String userRole = request.getHeader("X-Role");
        
        if (userRole == null) {
            throw new RuntimeException("User role not found in headers");
        }
        
        String[] allowedRoles = requireRole.value();
        boolean authorized = Arrays.asList(allowedRoles).contains(userRole);
        
        if (!authorized) {
            throw new RuntimeException("Access denied. Required roles: " +
                    String.join(" or ", allowedRoles) +
                    ". Your role: " + userRole);
        }
    }

    @Override
    public boolean supports(Class<?> annotationType) {
        return RequireRole.class.equals(annotationType);
    }
}
