package com.rohit.pg.simulator.service;

import com.rohit.pg.simulator.dto.VerifyVpaResponse;
import java.util.Map;

public class VpaService {

  private final Map<String, String> vpaMap =
      Map.of("rohit@upi", "Rohit Sharma", "test@upi", "Test User");

  public VerifyVpaResponse verify(String authToken, String vpa) {}
}
