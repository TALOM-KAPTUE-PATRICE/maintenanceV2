package com.kaptue.dev.maintenance.controller;

import com.kaptue.dev.maintenance.controller.dto.DemandeAchatPdfDTO;
import com.kaptue.dev.maintenance.controller.request.CreateDemandeAchatRequest; // Utiliser un DTO de requête
import com.kaptue.dev.maintenance.controller.request.UpdateDemandeAchatRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateStatusRequest;
import com.kaptue.dev.maintenance.controller.response.DemandeAchatResponseDTO; // Utiliser un DTO de réponse
import com.kaptue.dev.maintenance.entity.User; // Importer User
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.*; // Importer tous les services nécessaires
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demande-achats") // Convention de nommage et préfixe API
public class DemandeAchatController {

    private static final Logger logger = LoggerFactory.getLogger(DemandeAchatController.class);

    @Autowired
    private DemandeAchatService demandeAchatService;

    @Autowired
    private ThymeleafService thymeleafService;

    @Autowired
    private PdfService pdfService; // Assurez-vous qu'il est utilisé ou retirez-le

    @Autowired
    private UserService userService; // Pour récupérer l'utilisateur authentifié

    /**
     * Crée une nouvelle demande d'achat.
     *
     * @param request DTO contenant les informations de la demande d'achat.
     * @param authentication Informations de l'utilisateur authentifié.
     * @return DemandeAchatResponseDTO de la demande créée.
     */
    @PostMapping
    // @PreAuthorize("hasAuthority('" + Permission.DEMANDE_ACHAT_CREATE + "')") 
    public ResponseEntity<DemandeAchatResponseDTO> createDemandeAchat(
            @Valid @RequestBody CreateDemandeAchatRequest request,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User initiateur = userService.findUserEntityByEmail(userDetails.getUsername());

        logger.info("Création d'une demande d'achat par l'initiateur: {}", initiateur.getEmail());

        DemandeAchatResponseDTO createdDemandeAchat = demandeAchatService.createDemandeAchat(request, initiateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDemandeAchat);
    }

    /**
     * Récupère toutes les demandes d'achats.
     *
     * @return Liste de DemandeAchatResponseDTO.
     */
    @GetMapping
    // @PreAuthorize("hasAuthority('" + Permission.DEMANDE_ACHAT_READ_ALL + "')") 
    public ResponseEntity<List<DemandeAchatResponseDTO>> getAllDemandesAchats() {
        logger.info("Récupération de toutes les demandes d'achats.");
        List<DemandeAchatResponseDTO> dtoList = demandeAchatService.findAllDemandesAchats();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Récupère une demande d'achat par son ID.
     *
     * @param id L'ID de la demande d'achat.
     * @return DemandeAchatResponseDTO.
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.DEMANDE_ACHAT_READ + "')") 
    public ResponseEntity<DemandeAchatResponseDTO> getDemandeAchatById(@PathVariable Long id) {
        logger.info("Récupération de la demande d'achat ID: {}", id);
        DemandeAchatResponseDTO dto = demandeAchatService.findDemandeAchatById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Met à jour une demande d'achat existante.
     *
     * @param id L'ID de la demande d'achat.
     * @param request DTO avec les informations de mise à jour.
     * @return DemandeAchatResponseDTO mise à jour.
     */
    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.DEMANDE_ACHAT_UPDATE + "')")
    public ResponseEntity<DemandeAchatResponseDTO> updateDemandeAchat(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDemandeAchatRequest request) { // Utilise le DTO de mise à jour
        
        DemandeAchatResponseDTO updatedDto = demandeAchatService.updateDemandeAchat(id, request);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Supprime une demande d'achat.
     *
     * @param id L'ID de la demande d'achat à supprimer.
     * @return ResponseEntity sans contenu.
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.DEMANDE_ACHAT_DELETE + "')") 
    public ResponseEntity<Void> deleteDemandeAchat(@PathVariable Long id) {
        logger.info("Suppression de la demande d'achat ID: {}", id);
        demandeAchatService.deleteDemandeAchat(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Met à jour le statut d'une demande d'achat.
     * Ex: "APPROUVEE", "REFUSEE", "ANNULEE"
     * @param id L'ID de la demande d'achat.
     * @param request Le DTO contenant le nouveau statut.
     * @return La demande d'achat mise à jour.
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('" + Permission.DEMANDE_ACHAT_CHANGE_STATUS + "')")
    public ResponseEntity<DemandeAchatResponseDTO> updateDemandeAchatStatus(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateStatusRequest request) {
        
        DemandeAchatResponseDTO updatedDemandeAchat = demandeAchatService.updateDemandeAchatStatus(id, request.getStatus());
        return ResponseEntity.ok(updatedDemandeAchat);
    }

    // La génération de PDF pour DemandeAchat nécessite que PdfService ait une méthode correspondante.
    @GetMapping("/{id}/pdf")
    // @PreAuthorize("hasAuthority('" + Permission.DEMANDE_ACHAT_READ + "') or hasAuthority('" + Permission.DEMANDE_ACHAT_READ_ALL + "')")
    public ResponseEntity<byte[]> generateDemandeAchatPdf(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findUserEntityByEmail(userDetails.getUsername());

        logger.info("Demande de génération PDF pour la DA ID: {} par l'utilisateur: {}", id, currentUser.getEmail());

        DemandeAchatPdfDTO dataDto = demandeAchatService.prepareDemandeAchatDataForPdf(id, currentUser);

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("da", dataDto);

        String htmlContent = thymeleafService.processHtmlTemplate("demande_achat_template.html", templateVariables);

        // ▼▼▼ CHANGEMENT ICI ▼▼▼
        // Définir l'URI de base pour que Flying Saucer puisse trouver les images dans 'static/'
        String baseUri = "classpath:/static/";
        byte[] pdfContents = pdfService.generatePdfFromHtml(htmlContent, baseUri);
        // ▲▲▲ FIN DU CHANGEMENT ▲▲▲

        if (pdfContents == null || pdfContents.length == 0) {
            logger.error("Échec de la génération du PDF pour la DA ID: {}", id);
            return ResponseEntity.internalServerError().body("Erreur interne lors de la génération du PDF.".getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("inline", "demande-achat-" + id + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok().headers(headers).body(pdfContents);
    }
}
