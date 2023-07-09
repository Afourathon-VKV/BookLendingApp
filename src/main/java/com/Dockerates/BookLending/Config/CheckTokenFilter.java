package com.Dockerates.BookLending.Config;

import com.Dockerates.BookLending.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CheckTokenFilter extends OncePerRequestFilter {
    // Will intercept HTTP requests only once before passing it to the next filter in the chain
    private final JwtService jwtService;

    // Checks for valid JWT in Auth header.
    //allow routes that don't need authentication, give error for others
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Inside checkTokenFilter");
        String jwtFromCookie = jwtService.getJwtFromCookie(request);
        if (jwtFromCookie == null || jwtFromCookie.equals("null")) {
            if(request.getMethod().equals("OPTIONS")){
                ;
            }
            else if(!request.getRequestURI().contains("/api/users")) throw new RuntimeException("Not authorized");
        }
        filterChain.doFilter(request, response);
    }
}