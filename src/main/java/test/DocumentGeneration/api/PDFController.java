package test.DocumentGeneration.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import test.DocumentGeneration.services.PDFGenerationService;
import test.DocumentGeneration.types.TemplateCompletionResult;

@RestController
public class PDFController {

    private final PDFGenerationService pdfGenerationService;

    public PDFController(PDFGenerationService pdfGenerationService) {
        this.pdfGenerationService = pdfGenerationService;
    }

    @PostMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf() {
        System.out.println("pdf requested");

        TemplateCompletionResult result = pdfGenerationService.completePDFGeneration();

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headersWithFileName("pdf_table"))
                .body(result.getCompletedDocument().toByteArray());
    }

    private HttpHeaders headersWithFileName(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/pdf");
        headers.set("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        return headers;
    }
}
