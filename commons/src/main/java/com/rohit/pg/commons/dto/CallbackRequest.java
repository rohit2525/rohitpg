package com.rohit.pg.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallbackRequest {

  private String txnId; // Transaction ID to correlate
  private String status; // SUCCESS / FAILURE
  private String bankTxnId; // Optional: Bank transaction ID (can be null for now)
}
