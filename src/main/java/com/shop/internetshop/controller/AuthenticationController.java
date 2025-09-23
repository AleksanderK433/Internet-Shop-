package com.shop.internetshop.controller;

import com.shop.internetshop.model.User;
import com.shop.internetshop.dto.LoginUserDto;
import com.shop.internetshop.dto.RegisterUserDto;
import com.shop.internetshop.dto.VerifyUserDto;
import com.shop.internetshop.response.LoginResponse;
import com.shop.internetshop.service.AuthenticationService;
import com.shop.internetshop.service.EmailService;
import com.shop.internetshop.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@Validated
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, EmailService emailService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // NOWY testowy endpoint
    @PostMapping("/test-email")
    public ResponseEntity<?> testEmail(@RequestParam String email) {
        try {
            String testHtml = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                        <h2 style="color: #007bff;">🎉 Test Email - Internet Shop</h2>
                        <p>Jeśli widzisz tę wiadomość, konfiguracja <strong>Mailpit</strong> działa poprawnie!</p>
                        <p><strong>Timestamp:</strong> %s</p>
                        <div style="background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p>📧 Sprawdź inne emaile na: <a href="http://localhost:8025">http://localhost:8025</a></p>
                        </div>
                        <p style="color: #666; font-size: 12px;">To jest testowa wiadomość z Mailpit</p>
                    </div>
                    """.formatted(java.time.LocalDateTime.now());

            emailService.sendVerificationEmail(email, "Test Email - Internet Shop", testHtml);
            return ResponseEntity.ok("✅ Email testowy wysłany na: " + email + "\n🌐 Sprawdź: http://localhost:8025");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Błąd: " + e.getMessage() + "\n💡 Sprawdź czy Mailpit jest uruchomiony!");
        }
    }
}