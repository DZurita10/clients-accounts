package com.dzurita.msv.accounts.service;

import com.dzurita.msv.accounts.model.Movements;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IMovementsService {
    Flux<Movements> getMovementsByAccountNumber(String accountNumber);
    Mono<Movements> saveMovements(Movements movements);
}
