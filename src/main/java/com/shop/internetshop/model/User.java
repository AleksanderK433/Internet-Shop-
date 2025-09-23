package com.shop.internetshop.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username nie może być pusty")
    @Size(min = 3, max = 50, message = "Username musi mieć od 3 do 50 znaków")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username może zawierać tylko litery, cyfry, _ i -")
    private String username;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Email musi mieć prawidłowy format")
    @Size(max = 100, message = "Email może mieć maksymalnie 100 znaków")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Hasło nie może być puste")
    @Size(min = 8, message = "Hasło musi mieć minimum 8 znaków")
    private String password;

    @Column(name = "verification_code")
    @Size(min = 6, max = 6, message = "Kod weryfikacyjny musi mieć 6 cyfr")
    @Pattern(regexp = "^[0-9]{6}$", message = "Kod weryfikacyjny musi składać się z 6 cyfr")
    private String verificationCode;

    @Column(name = "verification_expiration")
    @Future(message = "Data wygaśnięcia musi być w przyszłości")
    private LocalDateTime verificationCodeExpiresAt;

    private boolean enabled;

    //constructor for creating an unverified user
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    //default constructor
    public User(){
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    //TODO: add proper boolean checks
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}