package com.example.usertemplate.auth.controller;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usertemplate.auth.dto.LoginRequest;
import com.example.usertemplate.auth.dto.LoginResponse;
import com.example.usertemplate.auth.dto.RegisterRequest;
import com.example.usertemplate.auth.security.JwtTokenProvider;
import com.example.usertemplate.auth.service.AuthService;
import com.example.usertemplate.global.common.ApiResponse;
import com.example.usertemplate.global.exception.BusinessException;
import com.example.usertemplate.global.redis.*;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.entity.User;
import com.example.usertemplate.user.repository.UserRepository;

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
  private final RefreshTokenRepository tokenRepository;
  private final RefreshTokenService tokenService;
  private final BlacklistTokenService blacklistTokenService;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

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

  @Operation(summary = "Logout", description = "User logout")
  @PostMapping("token/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      @RequestHeader("Authorization") final String accessToken) {

    String token = extractToken(accessToken);

    // 엑세스 토큰으로 현재 Redis 정보 삭제
    tokenService.removeRefreshToken(token);

    // 블랙리스트에 등록
    long ttl = jwtTokenProvider.getRemainingMillis(token) / 1000;
    blacklistTokenService.addToBlacklist(token, ttl);

    return ResponseEntity.ok(ApiResponse.success("logout successful", null));
  }

  @Operation(summary = "Refresh token", description = "Refresh access token")
  @PostMapping("/token/refresh")
  public ResponseEntity<ApiResponse<LoginResponse>> refresh(
      @RequestHeader("Authorization") final String accessToken) {

    String token = extractToken(accessToken);
    // 액세스 토큰으로 Refresh 토큰 객체를 조회
    Optional<RefreshToken> refreshToken = tokenRepository.findByAccessToken(token);

    // RefreshToken이 존재하고 유효하다면 실행
    if (refreshToken.isPresent()
        && jwtTokenProvider.validateToken(refreshToken.get().getRefreshToken())) {
      // RefreshToken 객체를 꺼내온다.
      RefreshToken resultToken = refreshToken.get();
      String userId = resultToken.getId();
      User user =
          userRepository
              .findByEmail(userId)
              .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

      // 새로운 액세스토큰을 만든다.
      String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
      // 액세스 토큰의 값을 수정해준다.
      resultToken.updateAccessToken(newAccessToken);
      tokenRepository.save(resultToken);
      // 새로운 액세스 토큰을 반환해준다.
      return ResponseEntity.ok(
          ApiResponse.success(
              "Token refreshed", LoginResponse.of(newAccessToken, resultToken.getRefreshToken())));
    }

    return ResponseEntity.badRequest()
        .body(ApiResponse.<LoginResponse>error("Invalid refresh token"));
  }

  private String extractToken(String bearerToken) {
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
