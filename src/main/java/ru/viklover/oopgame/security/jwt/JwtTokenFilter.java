package ru.viklover.oopgame.security.jwt;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import ru.viklover.oopgame.security.user.UserDetailsService;

@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        Cookie jwtCookie = null;
        for (Cookie cookie : request.getCookies()) {
            if(("auth_token").equals(cookie.getName())) {
                jwtCookie = cookie;
                break;
            }
        }

        if (jwtCookie != null && jwtUtils.validateToken(jwtCookie.getValue())) {

            var userId = jwtUtils.getUserIdFromToken(jwtCookie.getValue());
            var userDetails = userDetailsService.loadUserById(userId);

            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
