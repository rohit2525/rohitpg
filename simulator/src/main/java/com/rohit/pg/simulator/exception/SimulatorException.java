package com.rohit.pg.simulator.exception;

public class SimulatorException extends RuntimeException {
  private final String code;

  public SimulatorException(String code, String message) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
