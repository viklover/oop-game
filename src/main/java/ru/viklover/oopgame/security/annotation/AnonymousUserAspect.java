package ru.viklover.oopgame.security.annotation;

import lombok.AllArgsConstructor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
@Component
@AllArgsConstructor
public class AnonymousUserAspect {

    HttpServletRequest request;
    HttpServletResponse response;

    @Around("@within(AnonymousUser) || @annotation(AnonymousUser)")
    public Object permissionsOnEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {

        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return joinPoint.proceed();
        }

        response.setStatus(HttpServletResponse.SC_SEE_OTHER);

        return "redirect:/";
    }
}