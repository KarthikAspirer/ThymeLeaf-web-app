package com.java.Themeleaf.controller;

import com.java.Themeleaf.entity.User;
import com.java.Themeleaf.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/signup")
    public String signup(User user, RedirectAttributes redirectAttributes) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "error", "Username already exists");
            return "redirect:/signup";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // ✅ Success message for login page
        redirectAttributes.addFlashAttribute(
                "success",
                "User registered successfully. Please login.");

        return "redirect:/login";
    }
    // Show reset password page
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "reset-password";
    }
    // Handle reset password
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String username,
            @RequestParam String secretKey,
            @RequestParam String newPassword,
            RedirectAttributes redirectAttributes) {

        User user = userRepository
                .findByUsernameAndSecretKey(username, secretKey)
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or secret key"));

        // Encode new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "success",
                "Password reset successful. Please login.");

        return "redirect:/login";
    }
}
