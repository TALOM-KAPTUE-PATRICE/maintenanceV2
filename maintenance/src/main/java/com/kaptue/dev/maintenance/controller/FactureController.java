package com.kaptue.dev.maintenance.controller;

import com.kaptue.dev.maintenance.controller.dto.FacturePdfDTO;
import com.kaptue.dev.maintenance.controller.request.CreateFactureRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateFactureRequest;
import com.kaptue.dev.maintenance.controller.response.FactureResponseDTO;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.FactureService;
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
@RequestMapping("/api/factures")
public class FactureController {

    @Autowired private FactureService factureService;
    @Autowired private PdfService pdfService;
    @Autowired private ThymeleafService thymeleafService;

    @PostMapping
    // @PreAuthorize("hasAuthority('" + Permission.FACTURE_CREATE + "')")
    public ResponseEntity<FactureResponseDTO> createFacture(@Valid @RequestBody CreateFactureRequest request) {
        FactureResponseDTO createdFacture = factureService.createFacture(request);
        return new ResponseEntity<>(createdFacture, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.FACTURE_UPDATE + "')")
    public ResponseEntity<FactureResponseDTO> updateFacture(@PathVariable String id, @Valid @RequestBody UpdateFactureRequest request) {
        FactureResponseDTO updatedFacture = factureService.updateFacture(id, request);
        return ResponseEntity.ok(updatedFacture);
    }
    

    @GetMapping
    // @PreAuthorize("hasAuthority('" + Permission.FACTURE_READ_ALL + "')")
    public ResponseEntity<List<FactureResponseDTO>> getAllFactures() {
        return ResponseEntity.ok(factureService.getAllFactures());
    }


    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.FACTURE_READ + "')")
    public ResponseEntity<FactureResponseDTO> getFactureById(@PathVariable String id) {
        return ResponseEntity.ok(factureService.getFactureById(id));
    }


    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.FACTURE_DELETE + "')")
    public ResponseEntity<Void> deleteFacture(@PathVariable String id) {
        factureService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/{id}/pdf")
    // @PreAuthorize("hasAuthority('" + Permission.FACTURE_READ + "')")
    public ResponseEntity<byte[]> generateFacturePdf(@PathVariable String id) {
        FacturePdfDTO dataDto = factureService.prepareDataForPdf(id);

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("facture", dataDto);
        String htmlContent = thymeleafService.processHtmlTemplate("facture_template.html", templateVariables);
        
        String baseUri = "classpath:/static/";
        byte[] pdfContents = pdfService.generatePdfFromHtml(htmlContent, baseUri);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("inline", "facture-" + dataDto.getId() + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(pdfContents);
    }
}