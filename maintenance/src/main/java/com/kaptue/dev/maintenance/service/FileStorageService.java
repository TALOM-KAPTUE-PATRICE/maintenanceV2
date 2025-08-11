package com.kaptue.dev.maintenance.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kaptue.dev.maintenance.exception.FileStorageException;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocationRoot;
    private final String fileStorageBaseUrl;

    public FileStorageService(
        @Value("${app.file.upload-dir}") String uploadDir,
        @Value("${app.base-url}") String baseUrl

    ) {
        // Log pour déboguer le répertoire de travail
        logger.info("Répertoire de travail courant (user.dir): {}", System.getProperty("user.dir"));

        this.fileStorageLocationRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        logger.info("Chemin de stockage des fichiers configuré à : {}", this.fileStorageLocationRoot);
        this.fileStorageBaseUrl = baseUrl;
        logger.info("URL de base pour les fichiers configurée à : {}", this.fileStorageBaseUrl);

        try {
            Files.createDirectories(this.fileStorageLocationRoot);
            logger.info("Répertoire de stockage racine créé (ou déjà existant).");
        } catch (Exception ex) {
            throw new FileStorageException("Impossible de créer le répertoire racine : " + this.fileStorageLocationRoot, ex);
        }
    }

        /**
     * ▼▼▼ AJOUTEZ CETTE MÉTHODE MANQUANTE ▼▼▼
     * Retourne l'URL de base du serveur, qui sera utilisée pour construire les liens
     * complets vers les fichiers stockés.
     * @return L'URL de base (ex: "http://localhost:8081").
     */
    public String getFileStorageBaseUrl() {
        return this.fileStorageBaseUrl;
    }

    public String storeFile(MultipartFile file, String subDirectory) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Impossible de stocker un fichier vide ou nul.");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Le nom de fichier contient une séquence de chemin invalide: " + originalFileName);
            }

            String fileExtension = StringUtils.getFilenameExtension(originalFileName);
            String uniqueFileName = UUID.randomUUID().toString() + (fileExtension != null ? "." + fileExtension : "");

            Path targetDirectory = this.fileStorageLocationRoot.resolve(StringUtils.cleanPath(subDirectory));
            Files.createDirectories(targetDirectory);

            Path targetLocation = targetDirectory.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Fichier sauvegardé avec succès à : {}", targetLocation);

            // Retourne le chemin relatif qui sera stocké en base de données.
            // Ex: "subDirectory/uuid.jpg"
            String relativePathForDb = Paths.get(StringUtils.cleanPath(subDirectory), uniqueFileName).toString().replace("\\", "/");
            logger.info("Chemin stocké en BD: {}", relativePathForDb);
            return relativePathForDb;

        } catch (IOException ex) {
            logger.error("Impossible de stocker le fichier {}. Erreur: {}", originalFileName, ex.getMessage(), ex);
            throw new FileStorageException("Impossible de stocker le fichier " + originalFileName, ex);
        }
    }

    public void deleteFile(String relativeFilePathInDb) {
        // Le code de suppression est correct.
        if (!StringUtils.hasText(relativeFilePathInDb)) {
            logger.warn("Tentative de suppression d'un fichier avec un chemin vide ou nul.");
            return;
        }
        try {
            Path filePath = this.fileStorageLocationRoot.resolve(relativeFilePathInDb).normalize();
            if (Files.deleteIfExists(filePath)) {
                logger.info("Fichier supprimé : {}", filePath);
            }
        } catch (IOException ex) {
            logger.error("Impossible de supprimer le fichier {}. Erreur: {}", relativeFilePathInDb, ex.getMessage(), ex);
        }
    }
}