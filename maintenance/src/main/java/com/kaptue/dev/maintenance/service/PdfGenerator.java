package com.kaptue.dev.maintenance.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.IOException;
import java.net.MalformedURLException;

public final class PdfGenerator {

    private static final Color PRIMARY_COLOR = new DeviceRgb(96, 93, 255);
    private static final Color TEXT_COLOR = new DeviceRgb(51, 51, 51);
    private static final Color BORDER_COLOR = new DeviceRgb(221, 221, 221);
    private static final Color FOOTER_TEXT_COLOR = new DeviceRgb(150, 150, 150);
    private static final String LOGO_PATH = "maintenance/src/main/resources/static/BETANG.JPG";

    private PdfGenerator() {}

    public static void addHeader(Document document, String documentTitle) throws IOException {
        Image logo;
        try {
            logo = new Image(ImageDataFactory.create(LOGO_PATH));
            logo.setWidth(UnitValue.createPointValue(60)).setHeight(UnitValue.createPointValue(60));
        } catch (MalformedURLException e) {
            throw new IOException("Chemin du logo invalide: " + LOGO_PATH, e);
        }

        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 3})).setWidth(UnitValue.createPercentValue(100));
        headerTable.addCell(new Cell().add(logo).setBorder(null));

        Paragraph companyInfo = new Paragraph("BETANG ENGINEERING S.A.R.L")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(14).setFontColor(PRIMARY_COLOR);
        Paragraph companyAddress = new Paragraph("B.P. 12345, Douala, Cameroun\nTél: (+237) 690 00 00 00")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)).setFontSize(8);
        Paragraph title = new Paragraph(documentTitle)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(20).setMarginTop(10);
        
        headerTable.addCell(new Cell().add(companyInfo).add(companyAddress).add(title).setTextAlignment(TextAlignment.RIGHT).setBorder(null));

        document.add(headerTable);
        document.add(new Paragraph("\n").setFontSize(5));
        Table separator = new Table(1).setWidth(UnitValue.createPercentValue(100));
        separator.addCell(new Cell().setBorder(null).setBorderTop(new SolidBorder(BORDER_COLOR, 1f)));
        document.add(new Paragraph("\n").setFontSize(10));
    }

    /**
     * Classe interne pour gérer l'événement de création de page et ajouter un pied de page.
     */
    protected static class FooterEventHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            
            try {
                // Créer un Canvas pour ajouter des éléments de haut niveau (Paragraph) au pied de page
                Canvas canvas = new Canvas(pdfCanvas, pageSize);
                PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

                float y = pageSize.getBottom() + 20; // Position verticale du pied de page

                // Ligne de séparation
                pdfCanvas.setStrokeColor(BORDER_COLOR).setLineWidth(0.5f).moveTo(36, y + 10).lineTo(pageSize.getWidth() - 36, y + 10).stroke();

                // Texte à gauche
                Paragraph footerText = new Paragraph("© " + java.time.Year.now() + " Maint-Pro | BETANG ENGINEERING S.A.R.L")
                        .setFont(font).setFontSize(8).setFontColor(FOOTER_TEXT_COLOR);
                canvas.showTextAligned(footerText, 36, y, TextAlignment.LEFT);

                // Numérotation de page à droite
                int pageNum = pdfDoc.getPageNumber(page);
                String pageNumberText = "Page " + pageNum + " sur " + pdfDoc.getNumberOfPages();
                canvas.showTextAligned(new Paragraph(pageNumberText).setFont(font).setFontSize(8).setFontColor(FOOTER_TEXT_COLOR),
                        pageSize.getWidth() - 36, y, TextAlignment.RIGHT);
                
                canvas.close();
            } catch (IOException e) {
                // Gérer l'exception de manière appropriée (ex: log)
                throw new RuntimeException("Erreur lors de la création du pied de page", e);
            }
        }
    }


    public static Paragraph createSectionTitle(String title) throws IOException {
        return new Paragraph(title)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(12)
                .setFontColor(PRIMARY_COLOR).setMarginTop(15).setMarginBottom(10)
                .setBorderBottom(new SolidBorder(BORDER_COLOR, 0.5f)).setPaddingBottom(5);
    }

    public static Table createInfoTable(String... keyValuePairs) throws IOException {
        if (keyValuePairs.length % 2 != 0) throw new IllegalArgumentException("Les paires clé/valeur doivent être en nombre pair.");
        
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2})).setWidth(UnitValue.createPercentValue(100)).setMarginBottom(15);
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        for (int i = 0; i < keyValuePairs.length; i += 2) {
            table.addCell(new Cell().add(new Paragraph(keyValuePairs[i] + " :")).setFont(boldFont).setFontColor(TEXT_COLOR).setTextAlignment(TextAlignment.RIGHT).setBorder(null).setPaddingRight(10));
            table.addCell(new Cell().add(new Paragraph(keyValuePairs[i+1] != null ? keyValuePairs[i+1] : "N/A")).setFont(regularFont).setFontColor(TEXT_COLOR).setBorder(null));
        }
        return table;
    }
}