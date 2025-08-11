package com.kaptue.dev.maintenance.controller;
import com.kaptue.dev.maintenance.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaptue.dev.maintenance.controller.request.CreateUserRequest;
import com.kaptue.dev.maintenance.controller.request.EmailRequest;
import com.kaptue.dev.maintenance.controller.request.LoginRequest;
import com.kaptue.dev.maintenance.controller.request.PasswordResetRequest; // Nouveau DTO
import com.kaptue.dev.maintenance.controller.response.UserResponseDTO;
import com.kaptue.dev.maintenance.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import com.kaptue.dev.maintenance.service.FileStorageService;
import com.kaptue.dev.maintenance.service.TokenBlacklistService;
import com.kaptue.dev.maintenance.service.UserService;
import jakarta.validation.Valid; // Pour la validation
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Pour l'upload d'image lors du register

import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Préfixe API
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService; // Pour récupérer le profil

    @Autowired
    private FileStorageService fileStorageService;
    /**
     * Endpoint pour la connexion des utilisateurs.
     * @param loginRequest Contient email et mot de passe.
     * @return ResponseEntity avec token et informations utilisateur.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Tentative de connexion pour l'email: {}", loginRequest.getEmail());
        Map<String, Object> response = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }


    /**
     * Endpoint pour l'enregistrement de nouveaux utilisateurs.
     * L'image de profil est OBLIGATOIRE.
     * @param userJson String JSON représentant l'objet CreateUserRequest.
     * @param file Fichier image de profil (obligatoire).
     * @return ResponseEntity avec l'utilisateur créé.
     */

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestPart("user") String userJson,
            @RequestPart(name = "file") MultipartFile file) { // ✨ 'required' est true par défaut

        logger.info("Tentative d'enregistrement (JSON reçu pour 'user'): {}", userJson);
        try {
            CreateUserRequest userRequest = new ObjectMapper().readValue(userJson, CreateUserRequest.class);
            logger.info("Enregistrement pour l'email: {}", userRequest.getEmail());

            if (file == null || file.isEmpty()) {
                throw new BadRequestException("Le fichier image de profil est obligatoire.");
            }

            // 1. Sauvegarder l'image
            String imagePath = fileStorageService.storeFile(file, "profile-images");
            logger.info("Image de profil sauvegardée à : {}", imagePath);

            // 2. Créer l'utilisateur avec le chemin de l'image
            UserResponseDTO registeredUser = authService.registerUser(userRequest, imagePath);

            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (JsonProcessingException e) {
            logger.error("Erreur de parsing JSON pour la partie 'user': {}", e.getMessage(), e);
            throw new BadRequestException("Format JSON invalide pour les données utilisateur: " + e.getMessage());
        }
        // Les autres exceptions (FileStorageException, DuplicateResourceException)
        // seront gérées par le GlobalExceptionHandler.
    }

    /**
     * Endpoint pour récupérer le profil de l'utilisateur authentifié.
     * @param authentication Informations d'authentification de Spring Security.
     * @return ResponseEntity avec les informations de l'utilisateur.
     */

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserResponseDTO userProfile = userService.findUserByEmail(userDetails.getUsername()); // Créer cette méthode dans UserService
        logger.info("Profil récupéré pour: {}", userDetails.getUsername());
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Endpoint pour demander la réinitialisation du mot de passe.
     * Prend un DTO pour la validation de l'email.
     * @param emailRequest DTO contenant l'email de l'utilisateur.
     * @return ResponseEntity avec un message de confirmation générique.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody EmailRequest emailRequest) {
        // La validation de l'email est faite par @Valid
        logger.info("Demande de réinitialisation de mot de passe pour: {}", emailRequest.getEmail());
        authService.initiatePasswordReset(emailRequest.getEmail());
        // Toujours retourner le même message de succès pour des raisons de sécurité.
        return ResponseEntity.ok(Map.of("message", "Si un compte est associé à cet email, un lien de réinitialisation a été envoyé."));
    }

    /**
     * Endpoint pour réinitialiser le mot de passe avec un token.
     * @param passwordResetRequest DTO contenant le token et le nouveau mot de passe.
     * @return ResponseEntity avec un message de succès.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        logger.info("Tentative de réinitialisation de mot de passe avec token.");
        authService.resetPassword(passwordResetRequest.getToken(), passwordResetRequest.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès."));
    }

        @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) { // <-- Changer le type de retour
        final String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            tokenBlacklistService.blacklistToken(jwt);
        }
        
        // ▼▼▼ CORRECTION ICI ▼▼▼
        // Retourner un objet JSON valide au lieu d'une simple chaîne
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie. Le token a été invalidé."));
    }
}