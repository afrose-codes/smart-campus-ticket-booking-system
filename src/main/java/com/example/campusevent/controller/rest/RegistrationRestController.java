package com.example.campusevent.controller.rest;

import com.example.campusevent.entity.Registration;
import com.example.campusevent.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationRestController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping
    public ResponseEntity<List<Registration>> getAllRegistrations() {
        return ResponseEntity.ok(registrationService.getAllRegistrations());
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<?> register(
            @PathVariable Long eventId,
            @Valid @RequestBody Registration registration) {
        try {
            Registration saved = registrationService.register(registration, eventId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/by-email")
    public ResponseEntity<List<Registration>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(registrationService.getRegistrationsByEmail(email));
    }
}
