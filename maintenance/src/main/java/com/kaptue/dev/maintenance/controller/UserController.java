package com.kaptue.dev.maintenance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaptue.dev.maintenance.controller.request.CreateUserRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateUserRequest;
import com.kaptue.dev.maintenance.controller.response.UserResponseDTO;
import com.kaptue.dev.maintenance.security.Permission; // Importer les permissions
import com.kaptue.dev.maintenance.service.FileStorageService; // Nouveau service pour gérer les fichiers
import com.kaptue.dev.maintenance.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService; // Injecter le service de stockage de fichiers

    /**
     * Récupère tous les utilisateurs. Nécessite la permission USER_MANAGE.
     * @return Liste des UserResponseDTO.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Récupération de tous les utilisateurs.");
        return ResponseEntity.ok(userService.findAllUsers());
    }

    /**
     * Récupère un utilisateur par son ID. Nécessite la permission USER_MANAGE.
     * @param id L'ID de l'utilisateur.
     * @return UserResponseDTO.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        logger.info("Récupération de l'utilisateur avec ID: {}", id);
        return ResponseEntity.ok(userService.findUserById(id));
    }

    /**
     * Crée un nouvel utilisateur. Nécessite la permission USER_MANAGE.
     * @param userJson String JSON représentant l'objet CreateUserRequest.
     * @param file Fichier image optionnel pour le profil.
     * @return UserResponseDTO de l'utilisateur créé.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "')")
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestPart("user") String userJson, // Le JSON des données utilisateur
            @RequestPart(name = "file", required = false) MultipartFile file) { // L'image est optionnelle
        logger.info("Tentative de création d'utilisateur.");
        try {
            CreateUserRequest createUserRequest = new ObjectMapper().readValue(userJson, CreateUserRequest.class);
            String imagePath = null;
            if (file != null && !file.isEmpty()) {
                imagePath = fileStorageService.storeFile(file, "profile-images"); // "users" est un sous-dossier suggéré
                logger.info("Image de profil sauvegardée : {}", imagePath);
            }
            UserResponseDTO createdUser = userService.createUser(createUserRequest, imagePath);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur: {}", e.getMessage(), e);
            // Ce sera géré par GlobalExceptionHandler, mais un log ici est utile.
            // On pourrait retourner une ResponseEntity.badRequest() plus spécifique si besoin.
            throw new RuntimeException("Erreur lors du traitement de la requête de création d'utilisateur: " + e.getMessage(), e);
        }
    }

    /**
     * Met à jour un utilisateur existant. Nécessite la permission USER_MANAGE.
     * @param id L'ID de l'utilisateur à mettre à jour.
     * @param userJson String JSON représentant l'objet UpdateUserRequest.
     * @param file Fichier image optionnel pour mettre à jour le profil.
     * @return UserResponseDTO de l'utilisateur mis à jour.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestPart("user") String userJson,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        logger.info("Tentative de mise à jour de l'utilisateur avec ID: {}", id);
        try {
            UpdateUserRequest updateUserRequest = new ObjectMapper().readValue(userJson, UpdateUserRequest.class);
            String newImagePath = null;
            if (file != null && !file.isEmpty()) {
                // Optionnel: supprimer l'ancienne image si elle existe avant de sauvegarder la nouvelle
                UserResponseDTO currentUser = userService.findUserById(id); // Pour obtenir l'ancien chemin
                if (currentUser.getImagePath() != null) {
                    fileStorageService.deleteFile(currentUser.getImagePath());
                }
                newImagePath = fileStorageService.storeFile(file, "profile-images");
                logger.info("Nouvelle image de profil sauvegardée : {}", newImagePath);
            }
            UserResponseDTO updatedUser = userService.updateUser(id, updateUserRequest, newImagePath);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'utilisateur {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Erreur lors du traitement de la requête de mise à jour d'utilisateur: " + e.getMessage(), e);
        }
    }


    /**
     * Supprime un utilisateur. Nécessite la permission USER_MANAGE.
     * @param id L'ID de l'utilisateur à supprimer.
     * @return ResponseEntity sans contenu.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Tentative de suppression de l'utilisateur avec ID: {}", id);
        // Optionnel: supprimer l'image de profil associée avant de supprimer l'utilisateur
        UserResponseDTO userToDelete = userService.findUserById(id);
        if (userToDelete.getImagePath() != null) {
            fileStorageService.deleteFile(userToDelete.getImagePath());
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}