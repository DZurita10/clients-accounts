package com.dzurita.msv.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(name = "CustomerResponse", description = "Customer Response")
@Data
@Builder
public class CustomerResponseDTO {
    private String name;
    private String gender;
    private String identification;
    private String address;
    private String phone;
    private Boolean state;
}
