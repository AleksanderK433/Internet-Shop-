package com.shop.internetshop.user.model.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum Role {
    USER(List.of("USER")),
    ADMIN(List.of("USER", "ADMIN"));

    private final List<String> authorities;

    Role(List<String> authorities) {
        this.authorities = authorities;
    }



}
