package com.kaptue.dev.maintenance.service;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import com.kaptue.dev.maintenance.controller.dto.TicketPdfDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    /**
     * Génère un PDF professionnel pour un ticket.
     *
     * @param ticketData Le DTO contenant toutes les informations nécessaires
     * pour le PDF.
     * @return Le contenu du PDF en bytes.
     */
    public byte[] generateTicketPdf(TicketPdfDTO ticketData) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            // ✨ AJOUTER LE GESTIONNAIRE D'ÉVÉNEMENTS POUR LE PIED DE PAGE ✨
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new PdfGenerator.FooterEventHandler());

            Document document = new Document(pdfDocument, PageSize.A4);
            document.setMargins(36, 36, 50, 36); // Marge du bas plus grande pour le pied de page

            // 1. En-tête
            PdfGenerator.addHeader(document, "FICHE D'INTERVENTION");

            // 2. Informations générales du ticket
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            document.add(PdfGenerator.createInfoTable(
                    "N° du Ticket", String.valueOf(ticketData.getId()),
                    "Date de Création", ticketData.getDateCreation().format(formatter),
                    "Statut", ticketData.getStatut() != null ? ticketData.getStatut() : "Nouveau"
            ));

            // 3. Objet et Description
            document.add(PdfGenerator.createSectionTitle("Objet de l'intervention"));
            document.add(new Paragraph(ticketData.getTitre()).setBold());
            document.add(new Paragraph(ticketData.getDescription()).setMarginTop(5).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.JUSTIFIED));

            // 4. Informations Client
            document.add(PdfGenerator.createSectionTitle("Informations du Client"));
            document.add(PdfGenerator.createInfoTable(
                    "Nom", ticketData.getClientNom(),
                    "Email", ticketData.getClientEmail(),
                    "Téléphone", ticketData.getClientNumero()
            ));

            // 5. Informations Intervenant/Demandeur
            document.add(PdfGenerator.createSectionTitle("Intervenant / Demandeur"));
            document.add(PdfGenerator.createInfoTable(
                    "Nom", ticketData.getUserNom(),
                    "Poste", ticketData.getUserPoste(),
                    "Email", ticketData.getUserEmail()
            ));

            document.close();
            logger.info("PDF généré avec succès pour le ticket ID: {}", ticketData.getId());
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Erreur lors de la génération du PDF pour le ticket ID {}: {}", ticketData.getId(), e.getMessage(), e);
            return null;
        }
    }

   /**
     * Génère un PDF à partir d'une chaîne de caractères HTML en utilisant Flying Saucer.
     *
     * @param htmlContent Le contenu HTML final (déjà traité par Thymeleaf).
     * @param baseUri     L'URI de base pour résoudre les ressources relatives (CSS, images).
     * @return Le contenu du PDF en bytes.
     */
    public byte[] generatePdfFromHtml(String htmlContent, String baseUri) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ITextRenderer renderer = new ITextRenderer();

            // Définir le document HTML à rendre
            // L'URI de base est CRUCIAL pour que Flying Saucer trouve les images et CSS
            renderer.setDocumentFromString(htmlContent, baseUri);
            renderer.layout();
            renderer.createPDF(outputStream);
            
            outputStream.close();
            logger.info("PDF généré avec succès en utilisant Flying Saucer.");
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            // Note: com.lowagie.text.DocumentException est une exception courante ici
            logger.error("Erreur lors de la conversion HTML vers PDF avec Flying Saucer: {}", e.getMessage(), e);
            return null;
        }
    }
}
