package com.lms.auth.aspect;

import com.lms.auth.annotation.RequireRole;
import com.lms.auth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final JwtService jwtService;

    private String extractToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("No request context found");
        }
        HttpServletRequest request = attributes.getRequest();

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid authorization token");
        }
        return authHeader.substring(7);
    }

    private String getUserRole() {
        String token = extractToken();
        return jwtService.extractRole(token);
    }

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        String userRole = getUserRole();
        String[] allowedRoles = requireRole.value();

        boolean authorized = Arrays.asList(allowedRoles).contains(userRole);

        if (!authorized) {
            throw new RuntimeException("Access denied. Required roles: " +
                    String.join(" or ", allowedRoles) +
                    ". Your role: " + userRole);
        }

        System.out.println("✅ Access granted for role: " + userRole);
    }
}
