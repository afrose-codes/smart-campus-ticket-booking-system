package com.example.campusevent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Exposes the Google Maps API key to all Thymeleaf templates via a model attribute.
 * Templates access it as ${googleMapsKey}.
 */
@ControllerAdvice
public class GlobalModelAttributes {

    @Value("${google.maps.api.key:placeholder}")
    private String googleMapsKey;

    @ModelAttribute("googleMapsKey")
    public String googleMapsKey() {
        return googleMapsKey;
    }
}
