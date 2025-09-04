package com.example.usertemplate.auth.dto;

import jakarta.validation.constraints.NotBlank;

/** 로그인 요청을 처리하는 LoginRequest */
public record LoginRequest(
    // username이 비어있으면 메시지
    @NotBlank(message = "Username is required") String username,
    // password가 비어있으면 메시지
    @NotBlank(message = "Password is required") String password) {
  public LoginRequest {
    // username에 공백이 존재하면
    if (username != null && username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be blank");
    }
    // password에 공백이 존재하면
    if (password != null && password.trim().isEmpty()) {
      throw new IllegalArgumentException("Password cannot be blank");
    }
  }
}
