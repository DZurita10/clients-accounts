package com.dzurita.msv.accounts.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class MovementsRegisterRequestDTO {
    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(min = 10, max = 20, message = "El número de cuenta debe tener entre 10 y 20 caracteres")
    private String accountNumber;

    @NotBlank(message = "El tipo de movimiento no puede estar en blanco")
    @Pattern(regexp = "CREDITO|DEBITO",message = "El tipo de movimiento de ser CREDITO o DEBITO")
    private  String movementType;

    @NotNull(message = "El valor del movimiento no puede ser nulo")
    @DecimalMin(value = "0.01", inclusive = false, message = "El valor del movimiento debe ser mayor a cero")
    private BigDecimal movementValue;
}
