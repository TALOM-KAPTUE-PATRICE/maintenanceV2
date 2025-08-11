package com.kaptue.dev.maintenance.controller;

import com.kaptue.dev.maintenance.controller.request.CreateFournisseurRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateFournisseurRequest;
import com.kaptue.dev.maintenance.controller.response.FournisseurResponseDTO;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.FournisseurService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fournisseurs") // Pluriel et cohérent avec les autres
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    @PostMapping
    // @PreAuthorize("hasAuthority('" + Permission.FOURNISSEUR_CREATE + "')")
    public ResponseEntity<FournisseurResponseDTO> createFournisseur(@Valid @RequestBody CreateFournisseurRequest request) {
        FournisseurResponseDTO createdFournisseur = fournisseurService.createFournisseur(request);
        return new ResponseEntity<>(createdFournisseur, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.FOURNISSEUR_UPDATE + "')")
    public ResponseEntity<FournisseurResponseDTO> updateFournisseur(@PathVariable Long id, @Valid @RequestBody UpdateFournisseurRequest request) {
        FournisseurResponseDTO updatedFournisseur = fournisseurService.updateFournisseur(id, request);
        return ResponseEntity.ok(updatedFournisseur);
    }

    @GetMapping
    // Il est important que les utilisateurs qui créent des BC puissent accéder à cette liste.
    // Vous pouvez décommenter et ajuster la permission si nécessaire.
    // @PreAuthorize("hasAuthority('" + Permission.FOURNISSEUR_READ_ALL + "')")
    public ResponseEntity<List<FournisseurResponseDTO>> getAllFournisseurs() {
        List<FournisseurResponseDTO> fournisseurs = fournisseurService.findAll();
        return ResponseEntity.ok(fournisseurs);
    }
    


    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.FOURNISSEUR_READ + "')")
    public ResponseEntity<FournisseurResponseDTO> getFournisseurById(@PathVariable Long id) {
        return ResponseEntity.ok(fournisseurService.findById(id));
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.FOURNISSEUR_DELETE + "')")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        fournisseurService.deleteFournisseur(id);
        return ResponseEntity.noContent().build();
    }
}