package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import services.PDFGenerationService;

@SpringBootApplication
public class DocumentGeneration {
    public static void main(String[] args) {
        System.out.println("DocumentGeneration started!");
        SpringApplication.run(DocumentGeneration.class, args);

        PDFGenerationService.completePDFGeneration();
    }
}