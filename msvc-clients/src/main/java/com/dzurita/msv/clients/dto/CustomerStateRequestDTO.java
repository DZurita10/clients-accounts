package com.dzurita.msv.clients.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerStateRequestDTO {
    @NotNull(message = "El estado es obligatorio")
    private Boolean state;
}
