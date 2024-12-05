package com.dzurita.msv.accounts.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
public class AccountRequestDTO {
    @NotBlank(message = "El numero de cuenta no debe estar vacio")
    @Size(min = 10, max = 20, message = "El numero de cuenta debe tener entre 10 a 20 caracteres")
    private String accountNumber;

    @NotBlank(message = "El tipo de cuenta no debe estar vacio")
    @Pattern(regexp = "AHORRO|CORRIENTE",message = "El tipo de movimiento de ser AHORRO o CORRIENTE")
    private String accountType;

    @NotNull(message = "El valor del saldo no puede ser nulo")
    @DecimalMin(value = "0.00", inclusive = false, message = "El valor del saldo debe ser mayor a cero")
    private BigDecimal balance;

    @NotNull(message = "El estado es obligatorio")
    private Boolean state;

    @NotBlank(message = "El numero de identificacion no puede estar vacio")
    @Size(min = 10, max = 10, message = "El numero de identificacio debe tener 10 caracteres")
    private String identification;
}
