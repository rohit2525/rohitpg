package com.rohit.pg.simulator.service;

import com.rohit.pg.simulator.dto.AuthTokenResponse;
import com.rohit.pg.simulator.dto.SessionTokenResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {

  private final Map<String, Instant> sessionStore = new ConcurrentHashMap<>();
  private final Map<String, Instant> authStore = new ConcurrentHashMap<>();

  public SessionTokenResponse generateSession(String clientId, String clientSecret) {

    String token = UUID.randomUUID().toString();
    Instant expiry = Instant.now().plus(Duration.ofMinutes(60));
    sessionStore.put(token, expiry);
    return new SessionTokenResponse(token, 3600);
  }

  public AuthTokenResponse generateAuth(String sessionToken) {

    Instant expiry = sessionStore.get(sessionToken);

    if (expiry.isBefore(Instant.now())) {
      throw new RuntimeException("Session Expired");
    }

    String authToken = UUID.randomUUID().toString();
    authStore.put(authToken, Instant.now().plus(Duration.ofMinutes(15)));
    return new AuthTokenResponse(authToken, 900);
  }
}
