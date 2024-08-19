package org.nexus.ideagenerator.core.services;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.nexus.ideagenerator.core.components.PDFRequest;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;

import static com.itextpdf.kernel.font.PdfFontFactory.createFont;

public class PDFManager {
    private static final String DEST = "src/main/resources/pdf/";

    public static HttpStatus create(PDFRequest pdfRequest) {
        try {
            PdfWriter writer = new PdfWriter(DEST + pdfRequest.getTitle() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(50, 50, 50, 50);

            // Setting fonts
            PdfFont fontTitle = createFont("Helvetica-Bold");
            PdfFont fontNormal = createFont("Helvetica");

            // Colors
            Color blackColor = new DeviceRgb(0, 0, 0);
            Color darkGrayColor = new DeviceRgb(60, 60, 60);

            // Title
            Paragraph pageTitle = new Paragraph(pdfRequest.getTitle())
                    .setFont(fontTitle)
                    .setFontSize(28)
                    .setFontColor(blackColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);

            document.add(pageTitle);

            // Slogan
        Paragraph pageSlogan = new Paragraph(pdfRequest.getSlogan())
                    .setFont(fontNormal)
                    .setFontSize(18)
                    .setFontColor(darkGrayColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(40);

            document.add(pageSlogan);

            StringBuilder tagsToUse = new StringBuilder();
            for (String tag : pdfRequest.getTags()) {
                tagsToUse.append("#").append(tag).append(" ");
            }


            // Sections with separate paragraphs for keyword and content
            addSection(document, fontTitle, fontNormal, Color.DARK_GRAY, "Pitch", pdfRequest.getPitch());
            addSection(document, fontTitle, fontNormal, Color.DARK_GRAY, "Description", pdfRequest.getDescription());
            addSection(document, fontTitle, fontNormal, Color.DARK_GRAY, "Difficulty to Implement", pdfRequest.getDifficulty());
            addSection(document, fontTitle, fontNormal, Color.DARK_GRAY, "Success Rate", pdfRequest.getSuccess());
            addSection(document, fontTitle, fontNormal, Color.DARK_GRAY, "API to Use", pdfRequest.getApiToUse());
            addSection(document, fontTitle, fontNormal, Color.DARK_GRAY, "Tags", tagsToUse.toString());

            // Close the document
            document.close();

            return HttpStatus.CREATED;

        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    private static void addSection(
            Document document,
            PdfFont fontTitle,
            PdfFont fontNormal,
            Color color,
            String keyword,
            String content
    ) {
        // Keyword paragraph
        Paragraph keywordParagraph = new Paragraph(keyword + ":")
                .setFont(fontTitle)
                .setFontSize(14)
                .setFontColor(color)
                .setBold()
                .setMarginBottom(5);

        document.add(keywordParagraph);

        // Content paragraph
        Paragraph contentParagraph = new Paragraph(content)
                .setFont(fontNormal)
                .setFontSize(12)
                .setFontColor(color)
                .setMarginBottom(15);

        document.add(contentParagraph);
    }
}