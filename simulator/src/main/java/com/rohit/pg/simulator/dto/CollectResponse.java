package com.rohit.pg.simulator.dto;

public record CollectResponse(
    String status, // PENDING / FAILURE
    String bankTxnId,
    ErrorResponse error // only for FAILURE cases
    ) {}
