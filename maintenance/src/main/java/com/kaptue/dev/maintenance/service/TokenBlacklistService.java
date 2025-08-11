package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    @Autowired
    private JwtUtil jwtUtil;

    // Un cache simple en mémoire pour stocker les tokens invalidés et leur date d'expiration.
    // Clé: le token JWT, Valeur: la date d'expiration du token.
    private final ConcurrentHashMap<String, Date> blacklist = new ConcurrentHashMap<>();

    /**
     * Ajoute un token à la liste noire (blacklist).
     * @param token Le token JWT à invalider.
     */
    public void blacklistToken(String token) {
        if (token != null && !jwtUtil.isTokenExpired(token)) {
            Date expirationDate = jwtUtil.extractAllClaims(token).getExpiration();
            blacklist.put(token, expirationDate);
        }
    }

    /**
     * Vérifie si un token est dans la liste noire.
     * @param token Le token à vérifier.
     * @return true si le token est blacklisté, false sinon.
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklist.containsKey(token);
    }

    /**
     * Tâche planifiée pour nettoyer les tokens expirés de la blacklist.
     * S'exécute toutes les heures (3600000 millisecondes).
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanUpExpiredTokens() {
        Date now = new Date();
        blacklist.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
}