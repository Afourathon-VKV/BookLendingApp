package com.Dockerates.BookLending.Config;

import com.Dockerates.BookLending.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    // Will intercept HTTP requests only once before passing it to the next filter in the chain
    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    // Checks for valid JWT in Auth header.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Inside JwtAuthFilter");
        String jwtFromCookie = jwtService.getJwtFromCookie(request);
        if (jwtFromCookie != null) {
            System.out.println("jwtFromCookie: " + jwtFromCookie);
            String userEmailFromCookie = jwtService.extractUserEmail(jwtFromCookie);
            System.out.println(userEmailFromCookie);
            if (userEmailFromCookie != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println(1);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmailFromCookie);
                System.out.println(userDetails);
                if (jwtService.isTokenValid(jwtFromCookie, userDetails)) {
                    System.out.println(2);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    System.out.println(userDetails.getAuthorities());
                    System.out.println(authToken);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        System.out.println("URL hit at: " + request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}