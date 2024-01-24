package com.copernic.manageVehicles;
import com.copernic.manageVehicles.services.EmailService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Controller
public class PDFController {
    
     // Autowired para inyectar el servicio de correo electrónico
    @Autowired
    private EmailService emailService;

    // Datos dinámicos
    String nombre = "Usuario " + "Repair ID"; // Cambiar a .get()    
    //PDF
     byte[] bytesPDF = null;
    
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
        //Enviar por email
        bytesPDF = byteArrayOutputStream.toByteArray();
        // Devolver el arreglo de bytes
        return byteArrayOutputStream.toByteArray();
        }
    }
    @PostMapping("/send-email")
    public String enviarCorreo() {
        // Lógica para enviar el correo electrónico
        try {
            // Llamada al método para enviar el PDF por correo electrónico
            sendPdfByEmail(bytesPDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pdf-generate"; 
    }
   // Método para enviar el PDF por correo electrónico
    private void sendPdfByEmail(byte[] pdfBytes) {
        try {
            emailService.sendPdfByEmail("raulfer2017@gmail.com", pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    




