package com.copernic.manageVehicles;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.io.InputStream;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PDFController {
    // Datos dinámicos
    String nombre = "Usuario"; // O puedes obtenerlo dinámicamente
    
    @GetMapping("/generar-pdf")
    public String generarPdf(Model model) {

        // Agregar datos al modelo
        model.addAttribute("nombre", nombre);

        // Devolver el nombre de la plantilla (Thymeleaf)
        return "pdf-generate";
    }
    @GetMapping("/generate-pdf")
    @ResponseBody
    public byte[] generatePdf(HttpServletResponse response) throws IOException {
        // Crear documento PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Reparaciones de "+nombre);
            contentStream.newLine(); // Nueva línea
            contentStream.showText("REPARACIONES DIA 17/01/2024");
            contentStream.endText();

            // Agregar una imagen al PDF
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/logo.png");
            byte[] imageBytes = IOUtils.toByteArray(inputStream);
            PDImageXObject image = PDImageXObject.createFromByteArray(document, imageBytes, "logo.png");
            contentStream.drawImage(image, 50, 500);
      
            contentStream.close();

            // Convertir el documento a un arreglo de bytes
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);

            // Configurar la respuesta HTTP
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Repair "+nombre +".pdf");

            // Devolver el arreglo de bytes
            return byteArrayOutputStream.toByteArray();
        }

    }
}
    




