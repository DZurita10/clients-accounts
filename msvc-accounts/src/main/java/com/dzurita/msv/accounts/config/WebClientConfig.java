package com.dzurita.msv.accounts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.
                builder()
                .baseUrl("http://msvc-clients:8002/api/v1/customers")
                .build();
    }
}
