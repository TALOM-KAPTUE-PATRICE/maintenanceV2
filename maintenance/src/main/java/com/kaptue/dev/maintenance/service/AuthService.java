package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.request.CreateUserRequest;
import com.kaptue.dev.maintenance.controller.response.UserResponseDTO;
import com.kaptue.dev.maintenance.entity.User;
import com.kaptue.dev.maintenance.exception.DuplicateResourceException;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.jwt.JwtUtil;
import com.kaptue.dev.maintenance.repository.UserRepository;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import com.kaptue.dev.maintenance.entity.PasswordResetToken; // Importer la nouvelle entité
import com.kaptue.dev.maintenance.repository.PasswordResetTokenRepository; // Importer le nouveau repo
import java.util.UUID; // Pour générer un token aléatoire
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PermissionMappingService permissionMappingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService; // Pour envoyer l'email de réinitialisation

    public Map<String, Object> authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            logger.warn("Échec de l'authentification pour l'email {}: {}", email, e.getMessage());
            throw new BadCredentialsException("Identifiants invalides", e);
        }

        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé après authentification réussie pour l'email: " + email));

        Set<String> permissions = permissionMappingService.getPermissionsForUser(user);
        String token = jwtUtil.generateToken(email, user.getRole().name(), user.getPoste(), permissions);

        UserResponseDTO userDto = new UserResponseDTO(
                user.getId(), user.getNom(), user.getEmail(), user.getNumeroTelephone(),
                user.getRole(), user.getPoste(), user.getImagePath(), permissions
        );

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userDto);

        logger.info("Utilisateur connecté: {}, Rôle: {}, Poste: {}, Permissions: {}",
                user.getEmail(), user.getRole().name(), user.getPoste(), permissions);
        return response;
    }

    /**
     * Enregistre un nouvel utilisateur avec son image de profil.
     *
     * @param request Les données de création de l'utilisateur.
     * @param imagePath Le chemin relatif vers l'image de profil sauvegardée.
     * @return L'utilisateur sauvegardé (sous forme de DTO).
     * @throws DuplicateResourceException si l'email est déjà utilisé.
     */
    @Transactional
    public UserResponseDTO registerUser(CreateUserRequest request, String imagePath) { // ✨ Accepte imagePath
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("L'email '" + request.getEmail() + "' est déjà utilisé.");
        }
        if (!StringUtils.hasText(request.getPoste())) {
            throw new IllegalArgumentException("Le poste de l'utilisateur est requis.");
        }
        if (!StringUtils.hasText(imagePath)) { // Si l'image est obligatoire
            throw new IllegalArgumentException("L'image de profil est requise pour l'enregistrement.");
        }

        User newUser = new User();
        newUser.setNom(request.getNom());
        newUser.setEmail(request.getEmail());
        newUser.setNumeroTelephone(request.getNumeroTelephone());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());
        newUser.setPoste(request.getPoste());
        newUser.setImagePath(imagePath); // ✨ DÉFINIR L'IMAGE PATH ICI

        User savedUser = userRepository.save(newUser);
        logger.info("Utilisateur enregistré: ID {}, Email {}, Poste {}, ImagePath: {}",
                savedUser.getId(), savedUser.getEmail(), savedUser.getPoste(), savedUser.getImagePath());

        Set<String> permissions = permissionMappingService.getPermissionsForUser(savedUser);
        return new UserResponseDTO(
                savedUser.getId(), savedUser.getNom(), savedUser.getEmail(), savedUser.getNumeroTelephone(),
                savedUser.getRole(), savedUser.getPoste(), savedUser.getImagePath(), permissions
        );
    }

    /**
     * Initie le processus de réinitialisation de mot de passe pour un
     * utilisateur. Génère un token (pourrait être un JWT spécifique avec une
     * courte durée de vie ou un token aléatoire stocké en BD) et envoie un
     * email à l'utilisateur avec un lien contenant ce token.
     *
     * @param email L'email de l'utilisateur demandant la réinitialisation.
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé avec
     * cet email.
     */

    @Transactional
    public void initiatePasswordReset(String email) {
        // Toujours répondre avec un succès apparent pour ne pas révéler si un email est enregistré ou non.
        userRepository.findByEmail(email).ifPresent(user -> {
            // Un utilisateur avec cet email existe, nous pouvons procéder.
            // Invalider les anciens tokens pour cet utilisateur
            passwordResetTokenRepository.findByUser(user).ifPresent(passwordResetTokenRepository::delete);

            String tokenString = UUID.randomUUID().toString(); // Générer un token simple et aléatoire
            PasswordResetToken passwordResetToken = new PasswordResetToken(tokenString, user);
            passwordResetTokenRepository.save(passwordResetToken);

            logger.info("Token de réinitialisation généré pour l'utilisateur: {}", email);

            // Envoyer l'email de manière asynchrone
            emailService.sendPasswordResetEmail(user.getEmail(), user.getNom(), tokenString);
        });

        // Si l'utilisateur n'existe pas, on ne fait rien mais on ne lève pas d'erreur.
        // Cela empêche l'énumération d'utilisateurs.
        logger.info("Demande de réinitialisation de mot de passe traitée pour l'email (apparent): {}", email);
    }

    /**
     * Réinitialise le mot de passe de l'utilisateur en utilisant le token
     * fourni. Le token est validé (existence, expiration) et est à usage unique
     * (supprimé après utilisation).
     *
     * @param token Le token de réinitialisation.
     * @param newPassword Le nouveau mot de passe en clair.
     * @throws BadCredentialsException si le token est invalide ou expiré.
     */
    @Transactional
    public void resetPassword(String token, String newPassword) {
        if (!StringUtils.hasText(token)) {
            throw new BadCredentialsException("Token de réinitialisation manquant.");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Token de réinitialisation invalide."));

        if (resetToken.isExpired()) {
            passwordResetTokenRepository.delete(resetToken); // Nettoyer le token expiré
            throw new BadCredentialsException("Token de réinitialisation a expiré.");
        }
        
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Le token a été utilisé, il doit être supprimé pour qu'il ne puisse pas être réutilisé.
        passwordResetTokenRepository.delete(resetToken);

        logger.info("Mot de passe réinitialisé avec succès pour l'utilisateur: {}", user.getEmail());
    }

    // Pour que UserService puisse y accéder
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
