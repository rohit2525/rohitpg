package com.rohit.pg.commons.dto;

public record PaymentResponse(
    String txnId, // Internal transaction ID from PG
    String status, // PENDING / SUCCESS / FAILURE
    String message // Optional message (e.g., "Payment initiated successfully")
    ) {}
