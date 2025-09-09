package com.example.usertemplate.global.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistTokenRepository extends CrudRepository<BlacklistToken, String> {
  boolean existsByAccessToken(String accessToken);
}
