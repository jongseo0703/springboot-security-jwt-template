package com.example.usertemplate.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usertemplate.auth.dto.LoginRequest;
import com.example.usertemplate.auth.dto.LoginResponse;
import com.example.usertemplate.auth.dto.RegisterRequest;
import com.example.usertemplate.auth.security.JwtTokenProvider;
import com.example.usertemplate.global.exception.BusinessException;
import com.example.usertemplate.global.redis.RefreshTokenService;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.entity.Role;
import com.example.usertemplate.user.entity.User;
import com.example.usertemplate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;

  @Override
  @Transactional
  public UserResponse register(RegisterRequest request) {
    log.info("Registering new user with username: {}", request.username());

    // 이미 유저가 있는지 확인
    if (userRepository.existsByUsername(request.username())) {
      throw new BusinessException("Username already exists", 409, "DUPLICATE_USERNAME");
    }

    // 이미 있는 이메일인지 확인
    if (userRepository.existsByEmail(request.email())) {
      throw new BusinessException("Email already exists", 409, "DUPLICATE_EMAIL");
    }

    User user =
        User.builder()
            .username(request.username())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.USER)
            .enabled(true)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .build();

    User savedUser = userRepository.save(user);
    log.info("User registered successfully with ID: {}", savedUser.getId());

    return UserResponse.from(savedUser);
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    log.info("Attempting login for username: {}", request.username());

    // 1. 사용자 조회
    User user =
        userRepository
            .findByUsername(request.username())
            .orElseThrow(
                () -> {
                  log.error("사용자를 찾을 수 없음: {}", request.username());
                  return new BusinessException(
                      "Invalid username or password", 401, "AUTHENTICATION_FAILED");
                });

    log.debug("사용자 조회 성공 - username: {}, id: {}", user.getUsername(), user.getId());

    // 2. 비밀번호 검증
    boolean passwordMatches = passwordEncoder.matches(request.password(), user.getPassword());
    log.debug(
        "입력 비밀번호: {}, DB 해시: {}", request.password(), user.getPassword().substring(0, 20) + "...");

    if (!passwordMatches) {
      log.error("비밀번호 불일치 for user: {}", request.username());
      throw new BusinessException("Invalid username or password", 401, "AUTHENTICATION_FAILED");
    }

    log.debug("비밀번호 검증 성공");

    // 3. JWT 토큰 생성
    try {
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

      String accessToken = jwtTokenProvider.generateAccessToken(authentication);
      String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

      log.info("Login successful for user: {}", user.getUsername());

      refreshTokenService.saveTokenInfo(user.getEmail(), refreshToken, accessToken);

      return LoginResponse.of(accessToken, refreshToken);

    } catch (Exception ex) { // JWT 생성 관련 예외만 처리
      log.error("JWT 생성 실패 for user: {}", user.getUsername(), ex);
      throw new BusinessException("Token generation failed", 500, "TOKEN_ERROR");
    }
  }
}
