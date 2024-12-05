package com.dzurita.msv.accounts.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class MovementsResponseDTO {
    private LocalDate movementDate;
    private String accountNumber;
    private Boolean state;
    private BigDecimal movementValue;
    private String movementType;
    private BigDecimal availableBalance;
    private BigDecimal initialBalance;
}
