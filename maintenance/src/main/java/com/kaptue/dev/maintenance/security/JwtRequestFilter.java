package com.kaptue.dev.maintenance.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.kaptue.dev.maintenance.jwt.JwtUtil;
import com.kaptue.dev.maintenance.service.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtRequestFilter extends GenericFilterBean {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService; // <-- INJECTEZ LE SERVICE

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        final String authorizationHeader = httpRequest.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Vérifiez si l'en-tête contient un token JWT
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extrait le token
            // ▼▼▼ AJOUTEZ CETTE VÉRIFICATION ▼▼▼
            if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                // Si le token est blacklisté, on le rejette immédiatement.
                chain.doFilter(request, response);
                return;
            }

            username = jwtUtil.extractEmail(jwt);
        }

        // Si l'utilisateur n'est pas déjà authentifié, vérifiez le token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // Validez le token
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                // Authentifiez l'utilisateur
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continuez avec la chaîne de filtres
        chain.doFilter(request, response);
    }
}