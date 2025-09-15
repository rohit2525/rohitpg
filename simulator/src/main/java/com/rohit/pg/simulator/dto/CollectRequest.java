package com.rohit.pg.simulator.dto;

import lombok.Data;

@Data
public class CollectRequest {
  private String txnId;
  private double amount;
  private String payerVpa;
  private String payeeVpa;
}
