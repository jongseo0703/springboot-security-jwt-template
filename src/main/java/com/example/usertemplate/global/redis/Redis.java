package com.example.usertemplate.global.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Redis 엔티티: 사용자 Refresh Token 저장용
 *
 * <p>- username을 key로 사용하며 Refresh Token을 value로 저장 - Redis TTL(Time To Live)을 14일(3600*24*14초)로 설정
 * - Spring Data Redis가 @RedisHash를 통해 자동 매핑
 *
 * <p>사용 예: - 로그인 시 Refresh Token 생성 후 Redis에 저장 - Access Token 만료 시 Refresh Token 확인 후 재발급
 */
@RedisHash(value = "MemberToken", timeToLive = 3600 * 24 * 14)
@AllArgsConstructor
@Getter
@Setter
public class Redis {

  // 사용자 고유 username, Redis Key 역할
  @Id private String username;
  // 사용자 Refresh Token
  private String refreshToken;
}
