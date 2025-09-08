package ru.itmo.lab1.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itmo.lab1.entity.User;
import ru.itmo.lab1.service.MainService;

import javax.naming.AuthenticationException;
import java.io.IOException;

@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final MainService mainService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new AuthenticationException("Authentication required");
            }

            String token = authHeader.substring(7);
            User user = mainService.authenticate(token);

            request.setAttribute("user", user);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed: " + e.getMessage());
        }
    }
}
