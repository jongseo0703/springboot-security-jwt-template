package com.example.usertemplate.global.redis;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlacklistTokenService {

  private final BlacklistTokenRepository blacklistTokenRepository;

  @Transactional
  public void addToBlacklist(String accessToken, long ttlInSeconds) {
    blacklistTokenRepository.save(new BlacklistToken(accessToken));
  }

  public boolean isBlacklisted(String accessToken) {
    return blacklistTokenRepository.existsByAccessToken(accessToken);
  }
}
