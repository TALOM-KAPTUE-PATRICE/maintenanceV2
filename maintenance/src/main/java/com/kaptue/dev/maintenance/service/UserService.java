package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.request.CreateUserRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateUserRequest;
import com.kaptue.dev.maintenance.controller.response.UserResponseDTO;
import com.kaptue.dev.maintenance.entity.User;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Importer PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    // AuthService est utilisé pour le hachage de mot de passe dans registerUser,
    // mais pour createUser via UserController, on injectera PasswordEncoder directement.
    // AuthService pourrait avoir une méthode publique pour hacher si besoin.
    @Autowired
    private PasswordEncoder passwordEncoder; // Injecter directement PasswordEncoder

    @Autowired
    private PermissionMappingService permissionMappingService;


    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Set<String> permissions = permissionMappingService.getPermissionsForUser(user);
                    return new UserResponseDTO(
                        user.getId(), user.getNom(), user.getEmail(), user.getNumeroTelephone(),
                        user.getRole(), user.getPoste(), user.getImagePath(), permissions);
                })
                .collect(Collectors.toList());
    }

    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        Set<String> permissions = permissionMappingService.getPermissionsForUser(user);
        return new UserResponseDTO(
            user.getId(), user.getNom(), user.getEmail(), user.getNumeroTelephone(),
            user.getRole(), user.getPoste(), user.getImagePath(), permissions
        );
    }

    /**
     * Récupère un utilisateur par son email et le retourne sous forme de DTO.
     * @param email L'email de l'utilisateur.
     * @return UserResponseDTO.
     * @throws ResourceNotFoundException si l'utilisateur n'est pas trouvé.
     */
    
    public UserResponseDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        Set<String> permissions = permissionMappingService.getPermissionsForUser(user);
        return new UserResponseDTO(
            user.getId(), user.getNom(), user.getEmail(), user.getNumeroTelephone(),
            user.getRole(), user.getPoste(), user.getImagePath(), permissions
        );
    }

    /**
     * Récupère une entité User par son email (pour usage interne aux services).
     * @param email L'email de l'utilisateur.
     * @return Entité User.
     * @throws ResourceNotFoundException si l'utilisateur n'est pas trouvé.
     */
    public User findUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
    }


    @Transactional
    public UserResponseDTO createUser(CreateUserRequest createUserRequest, String imagePath) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new com.kaptue.dev.maintenance.exception.DuplicateResourceException("L'email '" + createUserRequest.getEmail() + "' est déjà utilisé.");
        }
        if (createUserRequest.getPoste() == null || createUserRequest.getPoste().trim().isEmpty()) {
            throw new IllegalArgumentException("Le poste de l'utilisateur est requis.");
        }

        User userToSave = new User();
        userToSave.setNom(createUserRequest.getNom());
        userToSave.setEmail(createUserRequest.getEmail());
        userToSave.setNumeroTelephone(createUserRequest.getNumeroTelephone());
        userToSave.setPassword(passwordEncoder.encode(createUserRequest.getPassword())); // Hachage ici
        userToSave.setRole(createUserRequest.getRole());
        userToSave.setPoste(createUserRequest.getPoste());
        userToSave.setImagePath(imagePath);

        User savedUser = userRepository.save(userToSave);
        logger.info("Utilisateur créé : ID {}", savedUser.getId());
        Set<String> permissions = permissionMappingService.getPermissionsForUser(savedUser);
        return new UserResponseDTO(
            savedUser.getId(), savedUser.getNom(), savedUser.getEmail(), savedUser.getNumeroTelephone(),
            savedUser.getRole(), savedUser.getPoste(), savedUser.getImagePath(), permissions
        );
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserRequest updateUserRequest, String newImagePath) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));

        if (StringUtils.hasText(updateUserRequest.getEmail()) && !user.getEmail().equals(updateUserRequest.getEmail())) {
            if (userRepository.existsByEmail(updateUserRequest.getEmail())) {
                throw new com.kaptue.dev.maintenance.exception.DuplicateResourceException("L'email '" + updateUserRequest.getEmail() + "' est déjà utilisé par un autre utilisateur.");
            }
            user.setEmail(updateUserRequest.getEmail());
        }

        if (StringUtils.hasText(updateUserRequest.getNom())) {
            user.setNom(updateUserRequest.getNom());
        }
        if (StringUtils.hasText(updateUserRequest.getNumeroTelephone())) {
            user.setNumeroTelephone(updateUserRequest.getNumeroTelephone());
        }
        if (updateUserRequest.getRole() != null) {
            user.setRole(updateUserRequest.getRole());
        }
        if (StringUtils.hasText(updateUserRequest.getPoste())) {
            user.setPoste(updateUserRequest.getPoste());
        }
        if (StringUtils.hasText(newImagePath)) {
            user.setImagePath(newImagePath);
        }

        User updatedUser = userRepository.save(user);
        logger.info("Utilisateur mis à jour: ID {}", updatedUser.getId());
        Set<String> permissions = permissionMappingService.getPermissionsForUser(updatedUser);
        return new UserResponseDTO(
            updatedUser.getId(), updatedUser.getNom(), updatedUser.getEmail(), updatedUser.getNumeroTelephone(),
            updatedUser.getRole(), updatedUser.getPoste(), updatedUser.getImagePath(), permissions
        );
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id + " pour suppression.");
        }
        userRepository.deleteById(id);
        logger.info("Utilisateur supprimé: ID {}", id);
    }
}