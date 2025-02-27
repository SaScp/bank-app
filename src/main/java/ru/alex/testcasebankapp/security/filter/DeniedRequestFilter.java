package ru.alex.testcasebankapp.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DeniedRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getHeader(HttpHeaders.USER_AGENT).equals("curl")) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "curl - user agent is illegal");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
