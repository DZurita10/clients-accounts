package com.dzurita.msv.accounts.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountStateRequestDTO {
    @NotNull(message = "El estado es obligatorio")
    private Boolean state;
}
