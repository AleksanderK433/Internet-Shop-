package com.shop.internetshop.user.dto;

import com.shop.internetshop.user.model.User;
import com.shop.internetshop.user.model.enums.Role;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        Role role,
        boolean enabled,
        boolean verified,
        boolean locked,
        Instant createdAt,
        Instant updatedAt
) {

    /**
     * Konstruktor konwertujący z encji User
     */
    public static UserResponseDto fromUser(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.isVerified(),
                user.isLocked(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
