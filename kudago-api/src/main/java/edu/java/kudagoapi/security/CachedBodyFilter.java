package edu.java.kudagoapi.security;

import edu.java.kudagoapi.utils.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class CachedBodyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        CachedBodyHttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(request);
        filterChain.doFilter(cachedBodyRequest, response);
    }
}
