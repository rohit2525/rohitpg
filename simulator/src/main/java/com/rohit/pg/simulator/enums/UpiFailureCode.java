package com.rohit.pg.simulator.enums;

public enum UpiFailureCode {
  GENERIC_FAILURE("99", "Generic Failure"),
  INSUFFICIENT_FUNDS("98", "Insufficient Funds"),
  DAILY_LIMIT_EXCEEDED("97", "Daily Limit Exceeded"),
  VPA_BLOCKED("96", "VPA is blocked or inactive");
  ;

  private final String code;
  private final String message;

  UpiFailureCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String code() {
    return this.code;
  }

  public String message() {
    return this.message;
  }
}
