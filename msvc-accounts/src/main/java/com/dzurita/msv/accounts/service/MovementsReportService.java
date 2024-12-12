package com.dzurita.msv.accounts.service;

import com.dzurita.msv.accounts.exception.NotFoundException;
import com.dzurita.msv.accounts.model.Movements;
import com.dzurita.msv.accounts.repository.MovementsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MovementsReportService implements IMovementsReportService{
    private final MovementsRepository movementsRepository;
    private final AccountService accountService;

    public Flux<Movements> generateReport(String identification, LocalDate startDate, LocalDate finishDate) {
        return Mono.fromCallable(() -> {
            return movementsRepository.findMovementsByAccountCustomerIdentificationAndMovementDateBetween(
                                    identification,
                                    startDate,
                                    finishDate
                            );
                        }
                )
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable);
    }
}
