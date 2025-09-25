package com.shop.internetshop.user.service;

import com.shop.internetshop.email.service.EmailService;
import com.shop.internetshop.user.model.User;
import com.shop.internetshop.user.model.enums.Role;
import com.shop.internetshop.user.dto.LoginUserDto;
import com.shop.internetshop.user.dto.RegisterUserDto;
import com.shop.internetshop.user.dto.VerifyUserDto;
import com.shop.internetshop.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User signup(RegisterUserDto input) {
        // DODAJ WALIDACJĘ - sprawdź czy user już istnieje
        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new RuntimeException("Użytkownik z tym emailem już istnieje");
        }
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new RuntimeException("Użytkownik z tym username już istnieje");
        }

        logger.info("[SIGNUP] Rejestracja nowego użytkownika: {}", input.getEmail());

        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        // Role.USER jest już ustawiona w konstruktorze User
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);

        sendVerificationEmail(user);
        User savedUser = userRepository.save(user);

        logger.info("[SIGNUP] Użytkownik zarejestrowany pomyślnie: {} z rolą: {}",
                savedUser.getEmail(), savedUser.getRole());

        return savedUser;
    }

    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // DODAJ SPRAWDZENIE BANA
        if (user.isBanned()) {
            throw new RuntimeException("Konto zostało zablokowane. Skontaktuj się z administratorem.");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        logger.info("[LOGIN] Użytkownik zalogowany: {} z rolą: {}", user.getEmail(), user.getRole());
        return user;
    }

    public void verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);

                logger.info("[VERIFY] Konto zweryfikowane: {} z rolą: {}", user.getEmail(), user.getRole());
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // NOWA METODA - tworzenie admina (dla data.sql lub testów)
    public User createAdmin(String username, String email, String password) {
        logger.info("[ADMIN] Tworzenie konta administratora: {}", email);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Admin z tym emailem już istnieje");
        }

        User admin = new User(username, email, passwordEncoder.encode(password), Role.ADMIN);
        User savedAdmin = userRepository.save(admin);

        logger.info("[ADMIN] Administrator utworzony pomyślnie: {}", savedAdmin.getEmail());
        return savedAdmin;
    }

    private void sendVerificationEmail(User user) {
        String subject = "Weryfikacja konta - Internet Shop";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = """
                <html>
                <head><meta charset="UTF-8"></head>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                        <h2 style="color: #333; text-align: center;">🛍️ Witamy w Internet Shop!</h2>
                        <p style="font-size: 16px; color: #666;">Cześć <strong>%s</strong>!</p>
                        <p style="font-size: 16px; color: #666;">Dziękujemy za rejestrację. Aby aktywować swoje konto, wprowadź poniższy kod weryfikacyjny:</p>
                        
                        <div style="background-color: #f8f9fa; padding: 20px; border-radius: 5px; text-align: center; margin: 20px 0;">
                            <h3 style="color: #333; margin: 0;">Kod weryfikacyjny:</h3>
                            <p style="font-size: 28px; font-weight: bold; color: #007bff; letter-spacing: 3px; margin: 10px 0;">%s</p>
                        </div>
                        
                        <p style="font-size: 14px; color: #999; text-align: center;">
                            ⏰ Kod wygasa za 15 minut<br>
                            🛡️ Twoja rola: <strong>USER</strong><br><br>
                            Jeśli to nie Ty zarejestrowałeś to konto, zignoruj tę wiadomość.
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(user.getUsername(), verificationCode);

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
            logger.info("[EMAIL] Email weryfikacyjny wysłany do: {}", user.getEmail());
        } catch (MessagingException e) {
            logger.error("[EMAIL] Błąd wysyłania emaila do {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Nie udało się wysłać emaila weryfikacyjnego. Spróbuj ponownie później.");
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}