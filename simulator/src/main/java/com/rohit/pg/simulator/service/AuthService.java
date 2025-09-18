package com.rohit.pg.simulator.service;

import com.rohit.pg.simulator.dto.AuthTokenResponse;
import com.rohit.pg.simulator.dto.SessionTokenResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenValidator tokenValidator;

  public SessionTokenResponse generateSession(String clientId, String clientSecret) {

    String token = UUID.randomUUID().toString();
    tokenValidator.storeSession(token, clientId);
    return new SessionTokenResponse(token, 3600);
  }

  public AuthTokenResponse generateAuth(String sessionToken) {

    if (!tokenValidator.isValidSession(sessionToken)) {
      throw new RuntimeException("Invalid or expired session token");
    }
    String authToken = UUID.randomUUID().toString();
    tokenValidator.storeAuth(authToken, "pg-poc");
    return new AuthTokenResponse(authToken, 900);
  }
}
