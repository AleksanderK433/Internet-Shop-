package com.shop.internetshop.user.service;

import com.shop.internetshop.email.service.EmailService;
import com.shop.internetshop.user.model.User;
import com.shop.internetshop.user.model.enums.Role;
import com.shop.internetshop.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;



    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public Map<String, Object> getUserStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByEnabledTrue();
        long bannedUsers = userRepository.countByBannedTrue();
        long adminCount = userRepository.countByRole(Role.ADMIN);
        long userCount = userRepository.countByRole(Role.USER);

        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("bannedUsers", bannedUsers);
        stats.put("adminCount", adminCount);
        stats.put("userCount", userCount);
        stats.put("unverifiedUsers", totalUsers - activeUsers);

        logger.info("[ADMIN] Statystyki użytkowników pobrane - Total: {}, Active: {}, Banned: {}",
                totalUsers, activeUsers, bannedUsers);

        return stats;
    }

    public List<User> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        logger.info("[ADMIN] Pobrano {} użytkowników z rolą: {}", users.size(), role);
        return users;
    }

    public String banUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Użytkownik nie został znaleziony");
        }

        User user = optionalUser.get();

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Nie można zbanować administratora");
        }

        if (user.isBanned()) {
            throw new RuntimeException("Użytkownik jest już zbanowany");
        }

        user.setBanned(true);
        userRepository.save(user);

        logger.warn("[ADMIN] Użytkownik zbanowany: {} ({})", user.getEmail(), user.getId());

        return "Użytkownik " + user.getUsername() + " został zbanowany";
    }

    public String unbanUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Użytkownik nie został znaleziony");
        }

        User user = optionalUser.get();

        if (!user.isBanned()) {
            throw new RuntimeException("Użytkownik nie jest zbanowany");
        }

        user.setBanned(false);
        userRepository.save(user);

        logger.info("[ADMIN] Użytkownik odbanowany: {} ({})", user.getEmail(), user.getId());

        return "Użytkownik " + user.getUsername() + " został odbanowany";
    }

    public String toggleBan(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Użytkownik nie został znaleziony");
        }

        User user = optionalUser.get();

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Nie można zbanować administratora");
        }

        boolean wasBanned = user.isBanned();
        user.setBanned(!wasBanned);
        userRepository.save(user);

        String action = wasBanned ? "odbanowany" : "zbanowany";
        logger.info("[ADMIN] Użytkownik {}: {} ({})", action, user.getEmail(), user.getId());

        return "Użytkownik " + user.getUsername() + " został " + action;
    }

    public String changeUserRole(Long userId, Role newRole) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Użytkownik nie został znaleziony");
        }

        User user = optionalUser.get();
        Role oldRole = user.getRole();

        if (oldRole == newRole) {
            throw new RuntimeException("Użytkownik już ma tę rolę");
        }

        user.setRole(newRole);
        userRepository.save(user);

        logger.info("[ADMIN] Zmiana roli użytkownika: {} ({}) z {} na {}",
                user.getEmail(), user.getId(), oldRole, newRole);

        return "Rola użytkownika " + user.getUsername() + " zmieniona z " + oldRole + " na " + newRole;
    }

    public String deleteUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Użytkownik nie został znaleziony");
        }

        User user = optionalUser.get();

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Nie można usunąć administratora");
        }

        String username = user.getUsername();
        String email = user.getEmail();

        userRepository.delete(user);

        logger.warn("[ADMIN] Użytkownik usunięty: {} ({})", email, userId);

        return "Użytkownik " + username + " został usunięty";
    }
}
