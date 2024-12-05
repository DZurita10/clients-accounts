package com.dzurita.msv.accounts.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponseDTO {
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private Boolean state;
    private String customerName;
}
