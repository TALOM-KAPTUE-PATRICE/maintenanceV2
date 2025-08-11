package com.kaptue.dev.maintenance.controller;

import com.kaptue.dev.maintenance.controller.dto.DevisPdfDTO;
import com.kaptue.dev.maintenance.controller.request.CreateDevisRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateDevisRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateStatusRequest;
import com.kaptue.dev.maintenance.controller.response.DevisArticleResponseDTO;
import com.kaptue.dev.maintenance.controller.response.DevisResponseDTO;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.DevisService;
import com.kaptue.dev.maintenance.service.PdfService;
import com.kaptue.dev.maintenance.service.ThymeleafService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devis") // URI standard
public class DevisController {

    @Autowired private DevisService devisService;
    @Autowired private ThymeleafService thymeleafService;
    @Autowired private PdfService pdfService;

    @PostMapping
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_CREATE + "')")
    public ResponseEntity<DevisResponseDTO> createDevis(@Valid @RequestBody CreateDevisRequest request) {
        DevisResponseDTO createdDevis = devisService.createDevis(request);
        return new ResponseEntity<>(createdDevis, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_READ_ALL + "')")
    public ResponseEntity<List<DevisResponseDTO>> getAllDevis() {
        return ResponseEntity.ok(devisService.getAllDevis());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_READ_OWN + "') or hasAuthority('" + Permission.DEVIS_READ_ALL + "')")
    public ResponseEntity<DevisResponseDTO> getDevisById(@PathVariable String id) {
        return ResponseEntity.ok(devisService.getDevisById(id));
    }

    @PostMapping("/{devisId}/articles/{articleId}")
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_MANAGE_ARTICLES + "')")
    public ResponseEntity<DevisResponseDTO> addArticleToDevis(@PathVariable String devisId, @PathVariable Long articleId) {
        DevisResponseDTO updatedDevis = devisService.addArticleToDevis(devisId, articleId);
        return ResponseEntity.ok(updatedDevis);
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_READ_OWN + "') or hasAuthority('" + Permission.DEVIS_READ_ALL + "')")
    public ResponseEntity<byte[]> generateDevisPdf(@PathVariable String id) {
        // 1. Préparer les données
        DevisPdfDTO dataDto = devisService.prepareDevisDataForPdf(id);

        // 2. Traiter le template
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("devis", dataDto);
        String htmlContent = thymeleafService.processHtmlTemplate("devis_template.html", templateVariables);

        // 3. Convertir en PDF
        String baseUri = "classpath:/static/";
        byte[] pdfContents = pdfService.generatePdfFromHtml(htmlContent, baseUri);
        
        // 4. Retourner la réponse
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("inline", "devis-" + id + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(pdfContents);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_UPDATE + "')")
    public ResponseEntity<DevisResponseDTO> updateDevis(
            @PathVariable String id,
            @Valid @RequestBody UpdateDevisRequest request) {
        DevisResponseDTO updatedDevis = devisService.updateDevis(id, request);
        return ResponseEntity.ok(updatedDevis);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_DELETE + "')")
    public ResponseEntity<Void> deleteDevis(@PathVariable String id) {
        devisService.deleteDevis(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/articles")
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_READ_OWN + "') or hasAuthority('" + Permission.DEVIS_READ_ALL + "')")
    public ResponseEntity<List<DevisArticleResponseDTO>> getArticlesForDevis(@PathVariable String id) {
        List<DevisArticleResponseDTO> articles = devisService.getArticlesForDevis(id);
        return ResponseEntity.ok(articles);
    }

     /**
     * Met à jour le statut d'un devis.
     * Ex: "EN_REDACTION", "SOUMIS", "VALIDER", "REFUSER"
     * @param id L'ID du devis.
     * @param request Le DTO contenant le nouveau statut.
     * @return Le devis mis à jour.
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('" + Permission.DEVIS_CHANGE_STATUS + "')")
    public ResponseEntity<DevisResponseDTO> updateDevisStatus(
            @PathVariable String id, 
            @Valid @RequestBody UpdateStatusRequest request) {
        
        DevisResponseDTO updatedDevis = devisService.updateDevisStatus(id, request.getStatus());
        return ResponseEntity.ok(updatedDevis);
    }

    
}