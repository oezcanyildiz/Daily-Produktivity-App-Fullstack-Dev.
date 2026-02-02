package com.ozcnyldz.todo_app.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component - Removed to avoid circular dependency, defined in SecurityConfig
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Authorization Header holen
        String authHeader = request.getHeader("Authorization");

        // 2. Prüfen ob Token vorhanden ist
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Token extrahieren (ohne "Bearer ")
        String token = authHeader.substring(7);
        String userEmail = null;

        try {
            // 4. Email aus Token extrahieren
            userEmail = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            // Token ist ungültig oder abgelaufen
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Wenn Email vorhanden und noch nicht authentifiziert
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. UserDetails laden (CustomUserDetails)
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // 7. Token validieren
            if (jwtUtil.validateToken(token, userEmail)) {

                // 8. Authentication-Objekt erstellen
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // WICHTIG: CustomUserDetails als Principal
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // 9. Authentication in SecurityContext setzen
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 10. Weiter zum nächsten Filter
        filterChain.doFilter(request, response);
    }
}
