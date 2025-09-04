package com.example.usertemplate.user.dto;

import java.time.LocalDateTime;

import com.example.usertemplate.user.entity.Role;
import com.example.usertemplate.user.entity.User;

public record UserResponse(
    Long id,
    String username,
    String email,
    Role role,
    boolean enabled,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
  public static UserResponse from(User user) {
    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRole(),
        user.isEnabled(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }
}
