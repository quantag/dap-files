package com.quantag.DAP.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/public/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String idTokenString = authHeader.replaceFirst("Bearer ", "");

            GoogleTokenVerifier googleTokenVerifier = new GoogleTokenVerifier();
            String userId = googleTokenVerifier.verifyToken(idTokenString);

            request.setAttribute("userId", userId);
        }
        else {
            request.setAttribute("userId", "null");
        }

        filterChain.doFilter(request, response);
    }
}
