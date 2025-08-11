package com.kaptue.dev.maintenance.controller;

import com.kaptue.dev.maintenance.controller.request.CreateBonCommandeRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateBonCommandeRequest;
import com.kaptue.dev.maintenance.controller.response.BonCommandeResponseDTO;
import com.kaptue.dev.maintenance.controller.dto.BonCommandePdfDTO;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.BonCommandeFournisseurService;
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
@RequestMapping("/api/bon-commandes") // URI RESTful
public class BonCommandeFournisseurController {

    @Autowired
    private BonCommandeFournisseurService bonCommandeService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private ThymeleafService thymeleafService;

    @PostMapping
    // @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_CREATE + "')")
    public ResponseEntity<BonCommandeResponseDTO> createBonCommande(@Valid @RequestBody CreateBonCommandeRequest request) {
        BonCommandeResponseDTO createdBonCommande = bonCommandeService.createBonCommande(request);
        return new ResponseEntity<>(createdBonCommande, HttpStatus.CREATED);
    }

    @GetMapping
    // @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_READ_ALL + "')")
    public ResponseEntity<List<BonCommandeResponseDTO>> getAllBonCommandes() {
        return ResponseEntity.ok(bonCommandeService.getAllBonCommandes());
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_READ + "')")
    public ResponseEntity<BonCommandeResponseDTO> getBonCommandeById(@PathVariable String id) {
        return ResponseEntity.ok(bonCommandeService.getBonCommandeById(id));
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_DELETE + "')")
    public ResponseEntity<Void> deleteBonCommande(@PathVariable String id) {
        bonCommandeService.deleteBonCommande(id);
        return ResponseEntity.noContent().build();
    }

    // Dans BonCommandeController.java
    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_UPDATE + "')")
    public ResponseEntity<BonCommandeResponseDTO> updateBonCommande(@PathVariable String id, @Valid @RequestBody UpdateBonCommandeRequest request) {
        BonCommandeResponseDTO updatedBonCommande = bonCommandeService.updateBonCommande(id, request);
        return ResponseEntity.ok(updatedBonCommande);
    }

    @GetMapping("/{id}/pdf")
    // @PreAuthorize("hasAuthority('" + Permission.BON_COMMANDE_READ + "')")
    public ResponseEntity<byte[]> generateBonCommandePdf(@PathVariable String id) {
        // 1. Préparer les données
        BonCommandePdfDTO dataDto = bonCommandeService.prepareDataForPdf(id);

        // 2. Traiter le template
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("bc", dataDto);
        String htmlContent = thymeleafService.processHtmlTemplate("bon_commande_template.html", templateVariables);

        // 3. Convertir en PDF
        String baseUri = "classpath:/static/";
        byte[] pdfContents = pdfService.generatePdfFromHtml(htmlContent, baseUri);

        // 4. Retourner la réponse
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("inline", "bon-commande-" + dataDto.getId() + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(pdfContents);
    }

}
