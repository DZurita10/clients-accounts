package com.dzurita.msv.accounts.service;

import com.dzurita.msv.accounts.exception.BadRequestException;
import com.dzurita.msv.accounts.exception.NotFoundException;
import com.dzurita.msv.accounts.model.Account;
import com.dzurita.msv.accounts.model.Movements;
import com.dzurita.msv.accounts.repository.MovementsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@AllArgsConstructor
public class MovementsService implements IMovementsService{

    private final MovementsRepository movementsRepository;
    private final AccountService accountService;

    public Flux<Movements> getMovementsByAccountNumber(String accountNumber) {
        return Mono.fromCallable(() -> movementsRepository.findMovementsByAccountAccountNumber(accountNumber))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(optionalMovements -> optionalMovements.isEmpty() ?
                        Flux.error(new NotFoundException("No existen movimientos asociados a la cuenta")) :
                        Flux.fromIterable(optionalMovements)
                );
    }

    public Mono<Movements> saveMovements(Movements movements) {
        log.info("Movements: {}", movements);
        return accountService.findByAccountNumber(movements.getAccount().getAccountNumber())
                .flatMap(account -> isZero(movements)
                        .flatMap(validMovements -> processMovement(movements, account))
                );
    }

    private Mono<Movements> isZero(Movements movements) {
        if (movements.getMovementValue().compareTo(BigDecimal.ZERO) == 0) {
            return Mono.error(new BadRequestException("El movimiento debe tener valores superiores que 0"));
        }
        return Mono.just(movements);
    }

    private Mono<Movements> processMovement(Movements movements, Account account) {

        return switch (movements.getMovementType().toUpperCase()) {
            case "CREDITO" -> processCredit(movements, account);
            case "DEBITO" -> processDebit(movements, account);
            default -> Mono.error(new BadRequestException("El tipo de movimiento no es válido"));
        };
    }

    private Mono<Movements> processCredit(Movements movements, Account account) {
        log.info("Account: {}", account);
        return processMovementCommon(movements, account,
                account.getBalance().add(movements.getMovementValue()));
    }

    private Mono<Movements> processDebit(Movements movements, Account account) {
        if(account.getBalance().subtract(movements.getMovementValue()).compareTo(BigDecimal.ZERO) < 0) {
            log.info("Saldo no disponible");
            return Mono.error(new BadRequestException("Saldo no disponible”"));
        }
        return processMovementCommon(movements, account,
                account.getBalance().subtract(movements.getMovementValue()));
    }

    private Mono<Movements> processMovementCommon(Movements movements, Account account,
                                                  BigDecimal newBalance) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.parse(LocalDate.now().format(formatter), formatter);
        movements.setInitialBalance(account.getBalance());
        movements.setAvailableBalance(newBalance);
        movements.setMovementDate(today);
        movements.setState(true);
        movements.setAccount(account);
        log.info("Movements procces: {}", movements);
        log.info("Account procces: {}", account);
        return Mono.fromCallable(() -> movementsRepository.save(movements))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(savedMovement -> {
                    account.setBalance(newBalance);
                    return accountService.updateAccountBalance(account.getAccountNumber(), account.getBalance())
                            .thenReturn(savedMovement);
                });
    }
}
