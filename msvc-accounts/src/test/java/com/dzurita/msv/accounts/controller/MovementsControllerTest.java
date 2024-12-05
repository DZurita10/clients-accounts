package com.dzurita.msv.accounts.controller;

import com.dzurita.msv.accounts.dto.MovementsReportResponseDTO;
import com.dzurita.msv.accounts.service.MovementsService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@WebFluxTest(MovementsController.class)
public class MovementsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovementsService movementsService;

//    @Test
//    void getAllMovements_returnsFluxOfMovementsReportResponseDTO_givenValidAccountNumber() {
//        String accountNumber = "123456";
//        MovementsReportResponseDTO responseDTO = MovementsReportResponseDTO.builder().build();
//
//        Movements movements = new Movements();
//        given(movementsService.getMovementsByAccountNumber(accountNumber))
//                .willReturn(Flux.just(movements));
//
//        webTestClient.get()
//                .uri("/api/v1/movements/account_number/{accountNumber}", accountNumber)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(MovementsReportResponseDTO.class)
//                .hasSize(1)
//                .contains(responseDTO);
//    }

    @Test
    void getAllMovements_returnsEmptyFlux_givenNoMovementsForAccountNumber() {
        String accountNumber = "000000";

        given(movementsService.getMovementsByAccountNumber(accountNumber))
                .willReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/v1/movements/account_number/{accountNumber}", accountNumber)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovementsReportResponseDTO.class)
                .hasSize(0);
    }

    @Test
    void getAllMovements_returnsNotFound_givenInvalidPathVariable() {
        webTestClient.get()
                .uri("/api/v1/movements/account_number/")
                .exchange()
                .expectStatus().isNotFound();
    }
}