package com.rohit.pg.simulator.controller;

import com.rohit.pg.simulator.dto.AuthTokenResponse;
import com.rohit.pg.simulator.dto.SessionTokenResponse;
import com.rohit.pg.simulator.dto.VerifyVpaResponse;
import com.rohit.pg.simulator.service.AuthService;
import com.rohit.pg.simulator.service.CollectService;
import com.rohit.pg.simulator.service.VpaService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class UpiSimulatorController {

  private final AuthService authService;
  private final VpaService vpaService;
  private final CollectService collectService;

  @PostMapping("/session")
  public SessionTokenResponse createSession(@RequestBody Map<String, String> request) {
    return authService.generateSession(request.get("clientId"), request.get("clientSecret"));
  }

  @PostMapping("/auth")
  public AuthTokenResponse generateAuthToken(@RequestHeader("Session-Token") String sessionToken) {
    return authService.generateAuth(sessionToken);
  }

  @PostMapping("/verify-vpa")
  public VerifyVpaResponse verifyVpa(
      @RequestHeader("Auth-Token") String authToken, @RequestBody Map<String, String> request) {
    return vpaService.verify(authToken, request.get("vpa"));
  }
}
