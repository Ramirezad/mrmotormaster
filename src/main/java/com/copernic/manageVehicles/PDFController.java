package com.copernic.manageVehicles;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Controller
public class PDFController {
    // Datos dinámicos
    String nombre = "Usuario + ID Reparacion"; // O puedes obtenerlo dinámicamente
    
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
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
        ITextRenderer renderer = new ITextRenderer();

        // Ruta al archivo HTML (ubicado en src/main/resources/templates)
        String templatePath = "templates/PlantillaPDF.html";

        try (InputStream inputStream = new ClassPathResource(templatePath).getInputStream()) {
            // Cargar contenido HTML desde la plantilla
            byte[] htmlBytes = IOUtils.toByteArray(inputStream);
            String htmlContent = new String(htmlBytes);

            // Establecer el contenido HTML en el ITextRenderer
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            try {
                renderer.createPDF(byteArrayOutputStream);
            } catch (DocumentException ex) {
                Logger.getLogger(PDFController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Configurar la respuesta HTTP
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Repair " + nombre + ".pdf");

        // Devolver el arreglo de bytes
        return byteArrayOutputStream.toByteArray();
        }
    }
}
    




