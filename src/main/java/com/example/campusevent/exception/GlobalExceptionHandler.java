package com.example.campusevent.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public String handleEventNotFound(EventNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Event Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(OverbookingException.class)
    public String handleOverbooking(OverbookingException ex, Model model) {
        model.addAttribute("errorTitle", "Booking Failed");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, Model model) {
        model.addAttribute("errorTitle", "Action Not Allowed");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        return "error";
    }

    /**
     * Catch-all for unexpected exceptions.
     * Logs the real cause so it appears in Eclipse console for debugging.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        // Print real stack trace to Eclipse console
        System.err.println("=== UNHANDLED EXCEPTION ===");
        ex.printStackTrace();
        System.err.println("===========================");

        model.addAttribute("errorTitle", "Something Went Wrong");
        model.addAttribute("errorMessage",
                "An error occurred: " + ex.getClass().getSimpleName()
                + " — " + ex.getMessage());
        return "error";
    }
}
