package com.rohit.pg.simulator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CollectRequest {
  @NotBlank(message = "Transaction ID is required")
  private String txnId;

  @NotBlank(message = "Amount is required")
  @Pattern(
      regexp = "^[0-9]+(\\.[0-9]{1,2})?$",
      message = "Amount must be a valid number with up to 2 decimal places")
  private String amount; // ✅ always string in JSON

  @NotBlank(message = "Payer VPA is required")
  @Pattern(
      regexp = "^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$",
      message = "Invalid payer VPA format")
  private String payerVpa;

  @NotBlank(message = "Payee VPA is required")
  @Pattern(
      regexp = "^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$",
      message = "Invalid payee VPA format")
  private String payeeVpa;
}
