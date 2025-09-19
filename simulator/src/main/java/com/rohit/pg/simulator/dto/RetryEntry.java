package com.rohit.pg.simulator.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class RetryEntry {

  private final CallbackRequest callbackRequest;
  private int retryCount;
  private LocalDateTime nextRetryTime;

  public RetryEntry(CallbackRequest callbackRequest) {
    this.callbackRequest = callbackRequest;
    this.retryCount = 0;
    this.nextRetryTime = LocalDateTime.now().plusSeconds(10); // first retry after 10 seconds
  }

  public void incrementRetry() {
    retryCount++;
    long delay = (long) Math.pow(2, retryCount); // exponential backoff
    this.nextRetryTime = LocalDateTime.now().plusSeconds(delay);
  }
}
