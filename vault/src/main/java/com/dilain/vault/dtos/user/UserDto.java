package com.dilain.vault.dtos.user;

import com.dilain.vault.entities.User;

public record UserDto(Long id, String username) {
    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}
