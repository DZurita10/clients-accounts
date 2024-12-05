package com.dzurita.msv.clients.controller;

import com.dzurita.msv.clients.dto.CustomerRequestDTO;
import com.dzurita.msv.clients.dto.CustomerResponseDTO;
import com.dzurita.msv.clients.dto.CustomerStateRequestDTO;
import com.dzurita.msv.clients.mapper.CustomerMapper;
import com.dzurita.msv.clients.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@AllArgsConstructor
@Api(tags = "Customer Management")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @ApiOperation(value = "Obtener lista de clientes", notes = "Este endpoint obtiene toda la lista de clientes registrados")
    public Flux<CustomerResponseDTO> getAllCustomers() {
        return customerService.findAll()
                .map(CustomerMapper::toCustomerResponseDTO);
    }

    @GetMapping("/{identification}")
    @ApiOperation(value = "Obtener cliente por numero de identificacion", notes = "Este endpoint obtiene un cliente de acuerdo a su numero de identificacion")
    public Mono<CustomerResponseDTO> getCustomerByIdentification(@PathVariable String identification) {
        return customerService.findById(identification)
                .map(CustomerMapper::toCustomerResponseDTO);
    }

    @PostMapping
    @ApiOperation(value = "Guardar cliente", notes = "Este endpoint registra un cliente")
    public Mono<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        return customerService.save(CustomerMapper.toCustomer(customerRequestDTO))
                .map(CustomerMapper::toCustomerResponseDTO);
    }

    @PatchMapping("/{identification}/state")
    @ApiOperation(value = "Borrar cliente", notes = "Este endpoint deshabilita un cliente")
    public Mono<CustomerResponseDTO> updateCustomerState(@PathVariable String identification, @RequestBody CustomerStateRequestDTO stateRequestDTO) {
        return customerService.changeCustomerState(identification, stateRequestDTO.getState())
                .map(CustomerMapper::toCustomerResponseDTO);
    }

    @PatchMapping("/{identification}")
    @ApiOperation(value = "Actualizar informacion del cliente", notes = "Este endpoint permite actualizar la informacion de un cliente")
    public Mono<CustomerResponseDTO> updateCustomer(@PathVariable String identification, @Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        return customerService.update(CustomerMapper.toCustomer(customerRequestDTO), identification)
                .map(CustomerMapper::toCustomerResponseDTO);

    }
}
