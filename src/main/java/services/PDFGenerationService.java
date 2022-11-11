package services;

import com.itextpdf.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import types.TemplateCompletionResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFGenerationService {
    static TemplateEngine templateEngine = new TemplateEngine();

    public static TemplateCompletionResult completePDFGeneration() {
        // Content for Template
        Context context = PDFDocumentUtil.setupData();
        // Template
        String templateBody = templateEngine.process("templates/tableDocument", context);
        System.out.println(templateBody);
        // Prepare pdf renderer
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(templateBody);

        try {
            renderer.getFontResolver().addFont("/fonts/Calibri.ttf", true);
        } catch (DocumentException e) {
            System.out.println("Error while writing in the Account Statement template");
        } catch (IOException e) {
            System.out.println("Error when font need to be added or font not found");
        }
        renderer.layout();

        // Generate the PDF and attach it to the response
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            renderer.createPDF(outputStream);
        } catch (DocumentException e) {
            System.out.println("Error while writing in the Account Statement template");
        } catch (IOException e) {
            System.out.println("Error while accessing the Account Statement template");
        }

        return TemplateCompletionResult.builder()
                .completedDocument(outputStream)
                .build();
    }
}
