package com.example.campusevent.service;

import com.example.campusevent.dto.RegisterDTO;
import com.example.campusevent.entity.User;
import com.example.campusevent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(RegisterDTO dto) {
        User user = new User();
        user.setFullName(dto.getFullName().trim());
        user.setUsername(dto.getUsername().trim().toLowerCase());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null && !dto.getRole().isBlank()
                ? dto.getRole() : "STUDENT");
        user.setEnabled(true);
        userRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username.trim().toLowerCase()).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase()).isPresent();
    }
}
