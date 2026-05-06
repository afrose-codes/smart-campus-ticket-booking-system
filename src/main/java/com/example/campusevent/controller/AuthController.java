package com.example.campusevent.controller;

import com.example.campusevent.dto.RegisterDTO;
import com.example.campusevent.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * GET /user-register
     * Shows the user account creation form.
     * Uses template: user-register.html
     * NOTE: /register is already used by RegistrationController for EVENT registration.
     */
    @GetMapping("/user-register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "user-register";
    }

    /**
     * POST /user-register
     * Processes user account creation.
     */
    @PostMapping("/user-register")
    public String processRegister(
            @Valid @ModelAttribute("registerDTO") RegisterDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Run DB duplicate checks only if field validation passed
        if (!result.hasErrors()) {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
            }
            if (userService.usernameExists(dto.getUsername())) {
                result.rejectValue("username", "duplicate",
                        "Username '" + dto.getUsername() + "' is already taken");
            }
            if (userService.emailExists(dto.getEmail())) {
                result.rejectValue("email", "duplicate",
                        "An account with this email already exists");
            }
        }

        if (result.hasErrors()) {
            return "user-register";
        }

        userService.registerUser(dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Account created successfully! You can now sign in.");
        return "redirect:/login?registered";
    }
}
