package com.rohit.pg.commons.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull
    private String merchantId;
    @NotNull
    private Double amount;
    private String mode;
    private String callbackUrl;
}
