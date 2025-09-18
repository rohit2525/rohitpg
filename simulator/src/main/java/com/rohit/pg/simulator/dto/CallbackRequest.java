package com.rohit.pg.simulator.dto;


public record CallbackRequest(
    String txnId, // Our PG’s transaction ID
    String bankTxnId, // Bank’s transaction ID
    String refId, // NPCI reference ID
    String payerVpa, // Who is paying
    String payeeVpa, // Who is receiving
    String amount, // Transaction amount
    String txnTime, // When bank processed it
    String status, // SUCCESS / FAILURE
    ErrorResponse error // Only present for FAILURE
    ) {}
