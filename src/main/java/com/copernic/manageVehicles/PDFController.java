package com.copernic.manageVehicles;

import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import com.copernic.manageVehicles.services.EmailService;
import com.copernic.manageVehicles.services.RepairService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Controller
public class PDFController {
    // Autowired para inyectar el servicio de correo electrónico

    @Autowired
    private EmailService emailService;

    // Autowired para inyectar el servicio de repair
    @Autowired
    private RepairService repairService;

    //PDF
    byte[] bytesPDF = null;

    @PostMapping("/generate-pdf-for-repair")
    @ResponseBody
    public byte[] generatePdfForRepair(@RequestParam Long repairId, HttpServletResponse response) throws IOException {
        // Obtener la reparación por ID
        Repair repair = repairService.findById(repairId);

        // Crear documento PDF para esta reparación
        try ( ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            // Convertir la reparación a HTML (puedes tener una plantilla específica para el PDF)
            String repairHtml = convertRepairToHtml(repair);

            // Establecer el contenido HTML en el ITextRenderer
            renderer.setDocumentFromString(repairHtml);
            renderer.layout();

            // Configurar la respuesta HTTP
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Repair_" + repair.getRepairId() + "_" + repair.getRepairDate() + ".pdf");

            // Enviar el PDF como bytes
            renderer.createPDF(byteArrayOutputStream);
            bytesPDF = byteArrayOutputStream.toByteArray();
            return byteArrayOutputStream.toByteArray();
        } catch (DocumentException ex) {
            Logger.getLogger(PDFController.class.getName()).log(Level.SEVERE, null, ex);
            // Manejo de errores, por ejemplo, redirigir a una página de error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    // Método para convertir la reparación a HTML (puedes personalizar según tus necesidades)
    private String convertRepairToHtml(Repair repair) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("images/logo.png");
        InputStream inputStream = classPathResource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(inputStream);
        return "<html><head></head><body>"
                + "<h1 style=\"position: relative;\">Repair " + repair.getVehicle().getBrand() + " " + repair.getVehicle().getModel() + " " + repair.getVehicle().getNumberPlate()
                + "<img src=\"data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes) + "\" alt=\"Descripción de la imagen\" style=\"position: absolute; right: 0; top: 0; width: 120px; height: 90px;\" /></h1>"
                + "<p>Repair num: " + "</p>"
                + "<p style=\"border: 1px solid black; padding: 5px;\"> " + repair.getRepairId() + "</p>"
                + "<p>Name: " + "</p>"
                + "<p style=\"border: 1px solid black; padding: 5px;\"> " + repair.getVehicle().getOwner().getName() + " " + repair.getVehicle().getOwner().getSurname() + "</p>"
                + "<p>Observation: " + "</p>"
                + "<p style=\"border: 1px solid black; padding: 5px;\"> " + repair.getObservation() + "</p>"
                + "<p>Kilometers: " + "</p>"
                + "<p style=\"border: 1px solid black; padding: 5px;\"> " + repair.getKm() + "</p>"
                + "<p>Date: " + "</p>"
                + "<p style=\"border: 1px solid black; padding: 5px;\"> " + repair.getRepairDate() + "</p>"
                + "<h2>Tasks for Repair</h2>"
                + "<table border=\"1\">"
                + "<tr>"
                + "<th>Name</th>"
                + "<th>Price</th>"
                + "</tr>"
                + repair.getTasks().stream()
                        .map(task -> "<tr><td>" + task.getName() + "</td><td>" + task.getPrice() + "€</td></tr>")
                        .collect(Collectors.joining())
                + "</table>"
                + "<h2>Total cost: " + repairService.getTotalPrice(repair.getRepairId()) + "€</h2>"
                + "</body></html>";
    }

    @PostMapping("/send-email")
    public String enviarCorreo(@RequestParam("repairId") Long repairId) {
        if (repairId == null) {
            return "redirect:/error";
        }
        Repair repair = repairService.findById(repairId);
        if (repair == null) {
            return "redirect:/error";
        }
        Vehicle vehicle = repair.getVehicle();
        User user = vehicle.getOwner();
        try {
            sendPdfByEmail(bytesPDF, user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/repairs";
    }
    // Método para enviar el PDF por correo electrónico

    private void sendPdfByEmail(byte[] pdfBytes, String mail) {
        try {
            emailService.sendPdfByEmail(mail, pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
