package com.kaptue.dev.maintenance.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    private final long EXPIRATION_TIME_AUTH = 1000 * 60 * 60 * 24; // 24 heures pour l'authentification
    private final long EXPIRATION_TIME_RESET = 1000 * 60 * 15;   // 15 minutes pour la réinitialisation de mdp
    private static final String CLAIM_KEY_PERMISSIONS = "permissions";
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_POSTE = "poste";
    private static final String CLAIM_KEY_IS_RESET_TOKEN = "is_reset";


    public String generateToken(String email, String role, String poste, Set<String> permissions) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ROLE, role);
        claims.put(CLAIM_KEY_POSTE, poste);
        claims.put(CLAIM_KEY_PERMISSIONS, permissions);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_AUTH))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generatePasswordResetToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_IS_RESET_TOKEN, true); // Marqueur pour identifier ce token

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_RESET)) // Expiration plus courte
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        // S'assurer que ce n'est pas un token de reset utilisé pour une authentification normale
        return (extractedEmail.equals(email) && !isTokenExpired(token) && !isPasswordResetToken(token));
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractPermissions(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get(CLAIM_KEY_PERMISSIONS, List.class);
    }

    public String extractRole(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get(CLAIM_KEY_ROLE, String.class);
    }

    public String extractPoste(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get(CLAIM_KEY_POSTE, String.class);
    }

    public boolean isPasswordResetToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return claims.get(CLAIM_KEY_IS_RESET_TOKEN, Boolean.class) != null && claims.get(CLAIM_KEY_IS_RESET_TOKEN, Boolean.class);
        } catch (Exception e) {
            return false; // Si le claim n'existe pas ou si le token est invalide
        }
    }

    public  Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) { // Rendre public si besoin ailleurs
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // Si le token est malformé ou invalide, considérez-le comme expiré/inutilisable
        }
    }
    
}