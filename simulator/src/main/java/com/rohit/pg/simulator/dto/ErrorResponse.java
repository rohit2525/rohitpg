package com.rohit.pg.simulator.dto;

public record ErrorResponse(
    String failureCode, // "99", "98", etc.
    String failureMessage // "Generic failure", "Insufficient funds", etc.
    ) {}
