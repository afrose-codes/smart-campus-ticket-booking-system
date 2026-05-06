package com.example.campusevent.controller;

import com.example.campusevent.entity.Registration;
import com.example.campusevent.repository.RegistrationRepository;
import com.example.campusevent.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoint to generate and download a QR code ticket for a registration.
 * GET /api/qr/{registrationId}?email={email}
 */
@RestController
@RequestMapping("/api/qr")
public class QRCodeController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/{registrationId}")
    public ResponseEntity<byte[]> getTicketQR(
            @PathVariable Long registrationId,
            @RequestParam(required = false) String email) {

        Registration reg = registrationRepository.findById(registrationId).orElse(null);
        if (reg == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate that the requesting email matches the registration
        if (email != null && !email.isBlank()
                && !reg.getEmail().equalsIgnoreCase(email.trim())) {
            return ResponseEntity.status(403).build();
        }

        String eventTitle = reg.getEvent() != null ? reg.getEvent().getTitle() : "Unknown Event";
        Long eventId = reg.getEvent() != null ? reg.getEvent().getId() : 0L;
        String eventDate = reg.getEvent() != null && reg.getEvent().getEventDate() != null
                ? reg.getEvent().getEventDate().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
                : "TBD";
        String venue = reg.getEvent() != null ? reg.getEvent().getVenue() : "TBD";

        // Human-readable content — displays clearly when scanned by any QR app
        String content = "=== SMARTCAMPUS EVENT TICKET ===\n"
                + "Ticket ID   : " + reg.getId() + "\n"
                + "Student     : " + reg.getStudentName() + "\n"
                + "Email       : " + reg.getEmail() + "\n"
                + "Department  : " + reg.getDepartment() + "\n"
                + "--------------------------------\n"
                + "Event       : " + eventTitle + "\n"
                + "Date & Time : " + eventDate + "\n"
                + "Venue       : " + venue + "\n"
                + "Tickets     : " + reg.getTicketsBooked() + "\n"
                + "--------------------------------\n"
                + "Verify at   : http://localhost:9090/admin/checkin?regId=" + reg.getId() + "\n"
                + "================================";

        try {
            byte[] qrImage = qrCodeService.generateQRCodeImage(content, 400, 400);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment",
                    "ticket-" + registrationId + ".png");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(qrImage);
        } catch (Exception e) {
            System.err.println("QR generation error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
