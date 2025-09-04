package com.example.usertemplate.auth.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usertemplate.auth.dto.LoginRequest;
import com.example.usertemplate.auth.dto.LoginResponse;
import com.example.usertemplate.auth.dto.RegisterRequest;
import com.example.usertemplate.auth.service.AuthService;
import com.example.usertemplate.global.common.ApiResponse;
import com.example.usertemplate.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** 로그인, 회원가입을 매핑하는 컨트롤러임. */
@Slf4j
@Tag(name = "Authentication", description = "User authentication APIs")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  // 회원가입을 처리하는 매핑임.
  @Operation(summary = "Register user", description = "Register a new user")
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<UserResponse>> register(
      @Valid @RequestBody RegisterRequest request) {
    try {
      UserResponse userResponse = authService.register(request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("User registered successfully", userResponse));
    } catch (Exception e) {
      log.error("Registration failed: ", e);
      throw e;
    }
  }

  // 로그인을 처리하는 매핑임.
  @Operation(summary = "Login", description = "User login")
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request) {
    try {
      LoginResponse response = authService.login(request);
      return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    } catch (Exception e) {
      log.error("Login failed: ", e);
      throw e;
    }
  }
}
