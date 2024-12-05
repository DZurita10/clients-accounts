package com.dzurita.msv.clients.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Schema(name = "CustomerRequest", description = "Customer Request")
@Data
public class CustomerRequestDTO {
    @NotBlank(message = "El nombre del cliente no puede estar vacio")
    @Size(min = 5, max = 64, message = "El nombre debe tener entre 5 a 64 caracteres")
    private String name;

    @NotBlank(message = "EL genero de la persona no puede estar vacio")
    @Pattern(regexp = "MASCULINO|FEMENINO", message = "El genero de la persona debe ser MASCULINO o FEMENINO")
    private String gender;

    @NotBlank(message = "El numero de identificacion no puede estar vacio")
    @Size(min = 10, max = 10, message = "El numero de identificacion debe tener 10 numeros")
    @Pattern(regexp = "^[0-9]+$", message = "Solo se permiten números del 0 al 9")
    private String identification;

    @NotBlank(message = "La direccion no puede estar vacia")
    @Size(min = 10, max = 128, message = "La direccion debe tener de 10 a 128 caracteres")
    private String address;

    @NotBlank(message = "El numero de telefono no puede estar vacio")
    @Pattern(regexp = "^[0-9]+$", message = "Solo se permiten números del 0 al 9")
    @Size(min = 10, max = 10, message = "El numero de telefono debe tener 10 numeros")
    private String phone;

    @NotBlank(message = "La contraseña no puede estar vacia")
    @Size(min = 12, max = 24, message = "La contraseña debe tener entre 12 a 24 caracteres")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{12,}$",
            message = "La contraseña debe contener al menos un número, un carácter especial y tener al menos 12 caracteres"
    )
    private String password;

    @NotNull(message = "El estado es obligatorio")
    private Boolean state;
}
