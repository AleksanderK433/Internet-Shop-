package com.shop.internetshop.user.web.controller;

import com.shop.internetshop.user.dto.UserRegistrationDto;
import com.shop.internetshop.user.dto.UserResponseDto;
import com.shop.internetshop.user.sevice.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserResponseDto userResponse = userService.registerUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestParam String username, @RequestParam String password) {
        // TODO: Implementacja logowania (na razie placeholder)
        // Spring Security automatycznie obsłuży uwierzytelnianie
        UserResponseDto userResponse = userService.findUserByUsername(username);
        return ResponseEntity.ok(userResponse);
    }


    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        UserResponseDto userResponse = userService.findUserByUsername(username);
        return ResponseEntity.ok(userResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Pomyślnie wylogowano");
    }


    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(!exists); // zwraca true jeśli username jest dostępny
    }


    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(!exists); // zwraca true jeśli email jest dostępny
    }
}
