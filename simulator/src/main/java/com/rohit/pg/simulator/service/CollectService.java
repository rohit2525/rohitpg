package com.rohit.pg.simulator.service;

import com.rohit.pg.simulator.dto.CallbackRequest;
import com.rohit.pg.simulator.dto.CollectRequest;
import com.rohit.pg.simulator.dto.CollectResponse;
import com.rohit.pg.simulator.dto.ErrorResponse;
import com.rohit.pg.simulator.enums.UpiFailureCode;
import com.rohit.pg.simulator.exception.SimulatorException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class CollectService {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
  private final TokenValidator tokenValidator;
  private final VpaService vpaService;
  // In-memory store: txnId -> status
  private final Map<String, String> collectStore = new ConcurrentHashMap<>();
  private final RestTemplate restTemplate = new RestTemplate();

  @PostMapping("/collect")
  public CollectResponse initiateCollect(
      @RequestHeader("Auth-Token") String authToken,
      @Valid @RequestBody CollectRequest collectRequest) {

    // 1. Validate Auth
    if (!tokenValidator.isValidAuth(authToken)) {
      throw new SimulatorException(
          String.valueOf(HttpServletResponse.SC_BAD_REQUEST), "Invalid Auth Token");
    }

    // 2. Validate Payer/Payee VPA
    var payerResult = vpaService.verify(authToken, collectRequest.getPayerVpa());

    if ("FAILURE".equals(payerResult.status())) {
      throw new SimulatorException("VPA_INVALID", "Invalid Payer VPA");
    }

    var payeeResult = vpaService.verify(authToken, collectRequest.getPayeeVpa());

    if ("FAILURE".equals(payeeResult.status())) {
      throw new SimulatorException("VPA_INVALID", "Invalid Payee VPA");
    }

    // 3. Generate txnId
    String bankTxnId = UUID.randomUUID().toString();

    // 4. process and return Success status for collect response,
    // also send 5 second delayed CallbackRequest

    new Thread(
            () -> {
              try {
                Thread.sleep(5000); // simulate processing
                sendCallback(collectRequest, bankTxnId);
              } catch (InterruptedException e) {
                log.error("Error in callback scheduling", e);
              }
            })
        .start();

    return new CollectResponse("SUCCESS", bankTxnId, null);
  }

  public UpiFailureCode handleFailure(String amount) {

    UpiFailureCode failure = null;

    failure =
        switch (amount) {
          case "99" -> UpiFailureCode.GENERIC_FAILURE;
          case "98" -> UpiFailureCode.INSUFFICIENT_FUNDS;
          case "97" -> UpiFailureCode.DAILY_LIMIT_EXCEEDED;
          case "96" -> UpiFailureCode.VPA_BLOCKED;
          default -> null;
        };

    return failure;
  }

  /** Send callback to Payment Engine. */
  private void sendCallback(CollectRequest collectRequest, String bankTxnId) {
    UpiFailureCode upiFailureCode = handleFailure(collectRequest.getAmount());
    String refId = UUID.randomUUID().toString();

    String status;
    ErrorResponse errorResponse = null;

    if (upiFailureCode != null) {
      // Failure scenario
      status = "FAILURE";
      errorResponse = new ErrorResponse(upiFailureCode.code(), upiFailureCode.message());
    } else {
      // Success scenario
      status = "SUCCESS";
    }

    CallbackRequest callbackRequest =
        new CallbackRequest(
            collectRequest.getTxnId(),
            bankTxnId,
            refId,
            collectRequest.getPayerVpa(),
            collectRequest.getPayeeVpa(),
            collectRequest.getAmount(),
            LocalDateTime.now().format(DATE_TIME_FORMATTER),
            status,
            errorResponse);

    String callbackUrl = "http://localhost:8080/pg/callback"; // TODO: externalize

    try {
      restTemplate.postForEntity(callbackUrl, callbackRequest, String.class);
      log.info("Callback sent txnId={} status={}", collectRequest.getTxnId(), status);
      collectStore.put(collectRequest.getTxnId(), callbackRequest.toString());
    } catch (Exception e) {
      log.error("Callback failed txnId={} will retry later", collectRequest.getTxnId(), e);
      // TODO: implement retry
    }
  }
}
