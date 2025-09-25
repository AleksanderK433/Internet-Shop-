package com.shop.internetshop.user.repository;

import com.shop.internetshop.user.model.enums.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.shop.internetshop.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);


    Optional<User> findByUsername(String username);


    List<User> findByRole(Role role);
    List<User> findByRoleAndEnabledTrue(Role role);
    long countByRole(Role role);


    List<User> findByBanned(boolean banned);
    List<User> findByEnabled(boolean enabled);


    // Znajdź zbanowanych użytkowników
    List<User> findByBannedTrue();

    // Znajdź aktywnych (niezbanowanych) użytkowników
    List<User> findByBannedFalse();

    // Liczba aktywnych (zweryfikowanych) użytkowników
    long countByEnabledTrue();

    // Liczba zbanowanych użytkowników
    long countByBannedTrue();

    // Liczba niezweryfikowanych użytkowników
    long countByEnabledFalse();

    // Liczba aktywnych użytkowników z daną rolą
    long countByRoleAndEnabledTrue(Role role);

    // Znajdź użytkowników po email pattern (dla wyszukiwania)
    List<User> findByEmailContainingIgnoreCase(String email);

    // Znajdź użytkowników po username pattern
    List<User> findByUsernameContainingIgnoreCase(String username);

    // Znajdź wszystkich oprócz adminów
    List<User> findByRoleNot(Role role);

}
