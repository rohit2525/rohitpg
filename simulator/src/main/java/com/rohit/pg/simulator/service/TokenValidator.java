package com.rohit.pg.simulator.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rohit.pg.simulator.dto.TokenInfo;
import com.rohit.pg.simulator.enums.TokenType;
import java.time.Duration;
import org.springframework.stereotype.Service;

@Service
public class TokenValidator {

  private final Cache<String, TokenInfo> sessionCache =
      Caffeine.newBuilder()
          .expireAfterWrite(Duration.ofMinutes(60)) // session tokens TTL
          .maximumSize(1000)
          .build();

  private final Cache<String, TokenInfo> authTokenCache =
      Caffeine.newBuilder()
          .expireAfterWrite(Duration.ofMinutes(60)) // session tokens TTL
          .maximumSize(1000)
          .build();

  public void storeSession(String token, String clientId) {
    sessionCache.put(token, new TokenInfo(TokenType.SESSION, clientId));
  }

  public void storeAuth(String token, String clientId) {
      authTokenCache.put(token, new TokenInfo(TokenType.AUTH, clientId));
  }

  public Boolean isValidSession(String token) {
    return sessionCache.getIfPresent(token) != null;
  }

  public Boolean isValidAuth(String token) {
    return authTokenCache.getIfPresent(token) != null;
  }
}
