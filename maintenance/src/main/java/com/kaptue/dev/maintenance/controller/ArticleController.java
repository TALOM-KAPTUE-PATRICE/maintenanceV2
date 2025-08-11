package com.kaptue.dev.maintenance.controller;

import com.kaptue.dev.maintenance.controller.request.CreateArticleRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateArticleRequest;
import com.kaptue.dev.maintenance.controller.response.ArticleResponseDTO;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    @PreAuthorize("hasAuthority('" + Permission.CATALOGUE_MANAGE + "')")
    public ResponseEntity<ArticleResponseDTO> createArticle(@Valid @RequestBody CreateArticleRequest request) {
        ArticleResponseDTO createdArticle = articleService.createArticle(request);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.CATALOGUE_MANAGE + "')")
    public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable Long id, @Valid @RequestBody UpdateArticleRequest request) {
        ArticleResponseDTO updatedArticle = articleService.updateArticle(id, request);
        return ResponseEntity.ok(updatedArticle);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('" + Permission.CATALOGUE_READ + "')")
    public ResponseEntity<Page<ArticleResponseDTO>> getAllArticles(
            @PageableDefault(size = 15, sort = "designation") Pageable pageable) {
        Page<ArticleResponseDTO> articles = articleService.findAllPaginated(pageable);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.CATALOGUE_READ + "')")
    public ResponseEntity<ArticleResponseDTO> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.CATALOGUE_MANAGE + "')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}