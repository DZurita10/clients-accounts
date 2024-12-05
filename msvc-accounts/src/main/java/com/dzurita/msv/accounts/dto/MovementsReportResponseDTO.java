package com.dzurita.msv.accounts.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class MovementsReportResponseDTO {
    private LocalDate date;
    private String customerName;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialValue;
    private Boolean status;
    private BigDecimal movementValue;
    private BigDecimal balance;
    private String movementType;
}
