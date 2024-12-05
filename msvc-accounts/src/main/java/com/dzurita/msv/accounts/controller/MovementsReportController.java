package com.dzurita.msv.accounts.controller;

import com.dzurita.msv.accounts.dto.MovementsReportResponseDTO;
import com.dzurita.msv.accounts.mapper.MovementMapper;
import com.dzurita.msv.accounts.service.MovementsReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Api(tags = "Movements report")
@RestController
@RequestMapping("/api/v1/reports")
public class MovementsReportController {

    private final MovementsReportService movementsReportService;

    @GetMapping("/{client-id}")
    @ApiOperation(value = "Generar reporte de movimientos bancarios", notes = "Genera un reporte detallado de los movimientos bancarios de un cliente especifico")
    public Flux<MovementsReportResponseDTO> getMovementsReport(
            @PathVariable("client-id") String clientId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
            ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return movementsReportService.generateReport(clientId, start, end)
                .map(MovementMapper::movementsToMovementsReportResponseDTO);

    }
}
