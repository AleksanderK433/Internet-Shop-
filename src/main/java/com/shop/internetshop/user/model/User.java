package com.shop.internetshop.user.model;

import com.shop.internetshop.user.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    @Size(min = 3, max = 20)
    private String username;

    @Column(unique = true, nullable = false)
    @Email(message = "Proszę podać prawidłowy adres e-mail")
    @NotBlank(message = "Adres e-mail jest wymagany")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Hasło jest wymagane")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    private boolean enabled = true;
    private boolean verified = false;
    private boolean locked = false;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant verifiedAt;
    private Instant lockedAt;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.enabled = true;
        this.verified = false;
        this.locked = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public User(String username, String email, String password, Role role) {
        this(username, email, password);
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority("ROLE_" + auth))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = Instant.now();
    }


    public void verify() {
        this.verified = true;
        this.verifiedAt = Instant.now();
        this.updatedAt = Instant.now();
    }


    public void lock() {
        this.locked = true;
        this.lockedAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
