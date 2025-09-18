package com.rohit.pg.simulator.service;

import com.rohit.pg.simulator.dto.VerifyVpaResponse;
import com.rohit.pg.simulator.exception.SimulatorException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VpaService {

  private final TokenValidator tokenValidator;

  private final Map<String, String> vpaMap =
      Map.of("rohit@upi", "Rohit Sharma", "test@upi", "Test User");

  public VerifyVpaResponse verify(String authToken, String vpa) {

    if (!tokenValidator.isValidAuth(authToken)) {
      throw new SimulatorException(
          String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "Invalid Auth Token");
    }

    if (vpaMap.containsKey(vpa)) {
      return new VerifyVpaResponse(vpa, vpaMap.get(vpa), "SUCCESS");
    } else {
      return new VerifyVpaResponse(vpa, "Vpa not found", "FAILURE");
    }
  }
}
