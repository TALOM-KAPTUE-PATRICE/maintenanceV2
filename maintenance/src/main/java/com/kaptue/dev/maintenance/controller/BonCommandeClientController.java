package com.kaptue.dev.maintenance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kaptue.dev.maintenance.controller.request.CreateBonCommandeClientRequest;
import com.kaptue.dev.maintenance.controller.response.BonCommandeClientResponseDTO;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.BonCommandeClientService;
import com.kaptue.dev.maintenance.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/bon-commandes-client")
public class BonCommandeClientController {

    @Autowired private BonCommandeClientService bccService;
    @Autowired private FileStorageService fileStorageService;

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_CLIENT_CREATE + "')")
    public ResponseEntity<BonCommandeClientResponseDTO> create(
            @RequestPart("bccData") String bccJson,
            @RequestPart("file") MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            CreateBonCommandeClientRequest request = mapper.readValue(bccJson, CreateBonCommandeClientRequest.class);
            
            String pdfPath = fileStorageService.storeFile(file, "b_commandes_client");
            
            BonCommandeClientResponseDTO createdBcc = bccService.create(request, pdfPath);
            return new ResponseEntity<>(createdBcc, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du bon de commande client: " + e.getMessage(), e);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_CLIENT_READ_ALL + "')")
    public ResponseEntity<List<BonCommandeClientResponseDTO>> getAll() {
        return ResponseEntity.ok(bccService.findAll());
    }

        // ▼▼▼ AJOUTEZ CES ENDPOINTS ▼▼▼
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_CLIENT_READ_ALL + "')")
    public ResponseEntity<BonCommandeClientResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bccService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_CLIENT_DELETE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bccService.delete(id);
        return ResponseEntity.noContent().build();
    }
}