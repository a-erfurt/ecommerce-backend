package org.ecommerce.backend.controllers;

import org.ecommerce.backend.models.User;
import org.ecommerce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/user/{id}/balance")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public BigDecimal getUserBalance(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getBalance();
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public User getUserProfile(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new User(user.getUsername(), user.getEmail(), user.getBalance());
    }
}
