package com.kaptue.dev.maintenance.controller;
import com.kaptue.dev.maintenance.controller.request.CreateCategorieRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateCategorieRequest;
import com.kaptue.dev.maintenance.controller.response.CategorieResponseDTO;
import com.kaptue.dev.maintenance.service.CategorieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    @Autowired
    private CategorieService categorieService;

    @PostMapping
    // @PreAuthorize("hasAuthority('" + Permission.CATEGORIE_MANAGE + "')")
    public ResponseEntity<CategorieResponseDTO> createCategorie(@Valid @RequestBody CreateCategorieRequest request) {
        CategorieResponseDTO createdCategorie = categorieService.createCategorie(request);
        return new ResponseEntity<>(createdCategorie, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.CATEGORIE_MANAGE + "')")
    public ResponseEntity<CategorieResponseDTO> updateCategorie(@PathVariable Long id, @Valid @RequestBody UpdateCategorieRequest request) {
        CategorieResponseDTO updatedCategorie = categorieService.updateCategorie(id, request);
        return ResponseEntity.ok(updatedCategorie);
    }

    @GetMapping
    // @PreAuthorize("hasAuthority('" + Permission.ARTICLE_READ + "')") // Tout utilisateur pouvant lire les articles doit pouvoir lire les catégories
    public ResponseEntity<List<CategorieResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categorieService.findAll());
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.ARTICLE_READ + "')")
    public ResponseEntity<CategorieResponseDTO> getCategorieById(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.findById(id));
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.CATEGORIE_MANAGE + "')")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.noContent().build();
    }
}