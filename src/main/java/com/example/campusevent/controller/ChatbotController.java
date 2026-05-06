package com.example.campusevent.controller;

import com.example.campusevent.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST endpoints for the AI chatbot widget.
 *
 * POST /api/chatbot/query   — original endpoint (kept for backward compat)
 * POST /api/chatbot/message — alias used by some frontend templates
 *
 * Both accept  { "message": "...", "studentEmail": "..." }
 * Both return  { "reply": "..." }
 */
@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    /** Original endpoint — used by index.html, events.html, etc. */
    @PostMapping("/query")
    public ResponseEntity<Map<String, String>> query(@RequestBody Map<String, String> body) {
        return handleChat(body);
    }

    /** Alias endpoint — used by templates that call /api/chatbot/message */
    @PostMapping("/message")
    public ResponseEntity<Map<String, String>> message(@RequestBody Map<String, String> body) {
        return handleChat(body);
    }

    private ResponseEntity<Map<String, String>> handleChat(Map<String, String> body) {
        String message = body.getOrDefault("message", "").trim();
        if (message.isEmpty()) {
            return ResponseEntity.ok(Map.of("reply", "Please type a message!"));
        }
        String reply = chatbotService.getResponse(message);
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}
