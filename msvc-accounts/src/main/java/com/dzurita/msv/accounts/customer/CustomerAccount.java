package com.dzurita.msv.accounts.customer;

import com.dzurita.msv.accounts.dto.CustomerResponseDTO;
import com.dzurita.msv.accounts.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
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
                .doOnError(error -> log.error(error.getMessage()))
                .onErrorMap(error -> new NotFoundException("No existe el usuario con identification " + identification));
    }
}
