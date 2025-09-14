package com.rohit.pg.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String txnId;      // Internal transaction ID from PG
    private String status;     // PENDING / SUCCESS / FAILURE
    private String message;    // Optional message (e.g., "Payment initiated successfully")
}
