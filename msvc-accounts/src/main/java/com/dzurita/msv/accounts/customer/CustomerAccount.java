package com.dzurita.msv.accounts.customer;

import com.dzurita.msv.accounts.dto.CustomerResponseDTO;
import com.dzurita.msv.accounts.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class CustomerAccount {
    private final WebClient webClient;

    public Mono<CustomerResponseDTO> getCustomerByIdentification(String identification) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{identification}")
                        .build(identification)
                )
                .retrieve()
                .bodyToMono(CustomerResponseDTO.class)
                .onErrorMap(error -> new NotFoundException("No existe el usuario con identification " + identification));
    }
}
