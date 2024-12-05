package com.dzurita.msv.accounts.controller;

import com.dzurita.msv.accounts.dto.MovementsRegisterRequestDTO;
import com.dzurita.msv.accounts.dto.MovementsReportResponseDTO;
import com.dzurita.msv.accounts.dto.MovementsResponseDTO;
import com.dzurita.msv.accounts.mapper.MovementMapper;
import com.dzurita.msv.accounts.service.MovementsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@AllArgsConstructor
@Api(tags = "Movements Manager")
@RestController
@RequestMapping("/api/v1/movements")
public class MovementsController {

    private final MovementsService movementsService;

    @GetMapping("/account_number/{accountNumber}")
    @ApiOperation(value = "Obtener todos los movimientos bancareos de una cuenta especifica", notes = "Obtiene todos los movimientos bancareos de una cuenta en especifico")
    public Flux<MovementsReportResponseDTO> getAllMovements(@PathVariable String accountNumber) {
        return movementsService.getMovementsByAccountNumber(accountNumber)
                .map(MovementMapper::movementsToMovementsReportResponseDTO);
    }

    @PostMapping
    @ApiOperation(value = "Registrar movimientos bancarios", notes = "Registra los movimientos bancarios de una cuenta")
    public Mono<MovementsResponseDTO> createMovement(@Valid @RequestBody MovementsRegisterRequestDTO movementsRegisterRequestDTO) {
        return Mono.fromCallable(() -> MovementMapper.movementRequestDTOToMovements(movementsRegisterRequestDTO))
                .flatMap(movementsService::saveMovements)
                .map(MovementMapper::movementToMovementsResponseDTO);
    }
}
