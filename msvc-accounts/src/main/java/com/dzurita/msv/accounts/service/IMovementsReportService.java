package com.dzurita.msv.accounts.service;

import com.dzurita.msv.accounts.model.Movements;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface IMovementsReportService {
    Flux<Movements> generateReport(String identification, LocalDate startDate, LocalDate finishDate);
}
