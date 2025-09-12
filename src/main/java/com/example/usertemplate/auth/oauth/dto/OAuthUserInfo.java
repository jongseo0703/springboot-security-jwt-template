package com.example.usertemplate.auth.oauth.dto;

import lombok.Builder;

@Builder
public record OAuthUserInfo(String oauthId, String email, String name, String provider) {

  public static OAuthUserInfo fromGoogle(String oauthId, String email, String name) {
    return OAuthUserInfo.builder()
        .oauthId(oauthId)
        .email(email)
        .name(name)
        .provider("google")
        .build();
  }

  public static OAuthUserInfo fromNaver(String oauthId, String email, String name) {
    return OAuthUserInfo.builder()
        .oauthId(oauthId)
        .email(email)
        .name(name)
        .provider("naver")
        .build();
  }

  public static OAuthUserInfo fromKakao(String oauthId, String email, String name) {
    return OAuthUserInfo.builder()
        .oauthId(oauthId)
        .email(email)
        .name(name)
        .provider("kakao")
        .build();
  }
}
