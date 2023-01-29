package ru.viklover.oopgame.security.annotations;

import lombok.AllArgsConstructor;

import jakarta.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Aspect
@Component
@AllArgsConstructor
public class LoginRequiredAspect {

    HttpServletResponse response;

    @Around("@within(LoginRequired) || @annotation(LoginRequired)")
    public Object permissionsOnEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {

        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            return joinPoint.proceed();
        }

        response.setStatus(HttpServletResponse.SC_SEE_OTHER);

        return "redirect:/login";
    }
}
