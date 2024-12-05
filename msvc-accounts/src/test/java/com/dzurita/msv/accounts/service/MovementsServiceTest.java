package com.dzurita.msv.accounts.service;

import com.dzurita.msv.accounts.exception.BadRequestException;
import com.dzurita.msv.accounts.exception.NotFoundException;
import com.dzurita.msv.accounts.model.Account;
import com.dzurita.msv.accounts.model.Movements;
import com.dzurita.msv.accounts.repository.MovementsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@Slf4j
public class MovementsServiceTest {
    @Mock
    private MovementsRepository movementsRepository;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private MovementsService movementsService;

    private final String accountNumber = "12345";
    private Movements movements;
    private Account account;
    private Account updatedAccount;
    private Movements savedMovement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.valueOf(100));
        account.setCustomerIdentification("12345");

        updatedAccount = new Account();
        updatedAccount.setAccountNumber("12345");
        updatedAccount.setBalance(BigDecimal.valueOf(1500));

        movements = new Movements();
        movements.setAccount(account);
        movements.setMovementValue(BigDecimal.valueOf(500));
        movements.setMovementType("CREDITO");

        LocalDate today = LocalDate.now();
        savedMovement = new Movements();
        savedMovement.setAccount(account);
        savedMovement.setMovementValue(BigDecimal.valueOf(500));
        savedMovement.setMovementType("CREDITO");
        savedMovement.setInitialBalance(BigDecimal.valueOf(1000));
        savedMovement.setAvailableBalance(BigDecimal.valueOf(1500));
        savedMovement.setMovementDate(today);
        savedMovement.setState(true);
    }

    @Test
    void findMovementsByAccountNumber_exist(){
        when(movementsRepository.findMovementsByAccountAccountNumber(accountNumber)).thenReturn(List.of(movements));

        Flux<Movements> result = movementsService.getMovementsByAccountNumber(accountNumber);

        StepVerifier.create(result)
                .expectNext(movements)
                .verifyComplete();

        verify(movementsRepository, times(1)).findMovementsByAccountAccountNumber(accountNumber);
    }

    @Test
    void findMovementsByAccountNumber_returnEmpty() {
        when(movementsRepository.findMovementsByAccountAccountNumber(accountNumber)).thenReturn(List.of());
        Flux<Movements> result = movementsService.getMovementsByAccountNumber(accountNumber);
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().equals("No existen movimientos asociados a la cuenta"))
                .verify();
        verify(movementsRepository, times(1)).findMovementsByAccountAccountNumber(accountNumber);
    }

    @Test
    void saveMovements_withZeroMovementValue() {
        movements.setMovementValue(BigDecimal.ZERO);
        when(accountService.findByAccountNumber(accountNumber)).thenReturn(Mono.just(account));
        Mono<Movements> result = movementsService.saveMovements(movements);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BadRequestException &&
                                throwable.getMessage().equals("El movimiento debe tener valores superiores que 0"))
                .verify();

        verify(accountService, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void saveMovements_withInvalidMovementType() {
        movements.setMovementType("INVALID");
        when(accountService.findByAccountNumber(accountNumber)).thenReturn(Mono.just(account));
        Mono<Movements> result = movementsService.saveMovements(movements);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BadRequestException &&
                                throwable.getMessage().equals("El tipo de movimiento no es válido"))
                .verify();

        verify(accountService, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void saveMovements_withInsufficientBalance_forDebit() {
        movements.setMovementType("DEBITO");
        movements.setMovementValue(BigDecimal.valueOf(2000));
        account.setBalance(BigDecimal.valueOf(100));
        when(accountService.findByAccountNumber(accountNumber)).thenReturn(Mono.just(account));
        Mono<Movements> result = movementsService.saveMovements(movements);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BadRequestException &&
                                throwable.getMessage().contains("Saldo no disponible"))
                .verify();

        verify(accountService, times(1)).findByAccountNumber(accountNumber);
    }


    @Test
    void saveMovements_withValidCredit() {
        movements.setMovementType("CREDITO");
        movements.setMovementValue(BigDecimal.valueOf(500));

        when(accountService.findByAccountNumber(accountNumber)).thenReturn(Mono.just(account));
        when(movementsRepository.save(any(Movements.class))).thenReturn(savedMovement);
        when(accountService.updateAccountBalance(anyString(), any(BigDecimal.class))).thenReturn(Mono.just(updatedAccount));
        log.info("Movements->: {}", updatedAccount);
        Mono<Movements> result = movementsService.saveMovements(movements);

        StepVerifier.create(result)
                .expectNextMatches(movement ->
                        movement.getAvailableBalance().equals(BigDecimal.valueOf(1500)) &&
                                movement.getState().equals(true)
                )
                .verifyComplete();

        verify(accountService, times(1)).findByAccountNumber(accountNumber);
        verify(movementsRepository, times(1)).save(any(Movements.class));
        verify(accountService, times(1)).updateAccountBalance(anyString(), any(BigDecimal.class));
    }

    @Test
    void saveMovements_withValidDebit() {
        movements.setMovementType("DEBITO");
        movements.setMovementValue(BigDecimal.valueOf(50));

        when(accountService.findByAccountNumber(accountNumber)).thenReturn(Mono.just(account));
        when(movementsRepository.save(any(Movements.class))).thenReturn(savedMovement);
        when(accountService.updateAccountBalance(anyString(), any(BigDecimal.class))).thenReturn(Mono.just(updatedAccount));

        Mono<Movements> result = movementsService.saveMovements(movements);

        StepVerifier.create(result)
                .expectNextMatches(movement ->
                        movement.getAvailableBalance().equals(BigDecimal.valueOf(1500)) &&
                                movement.getState().equals(true)
                )
                .verifyComplete();

        verify(accountService, times(1)).findByAccountNumber(accountNumber);
        verify(movementsRepository, times(1)).save(any(Movements.class));
        verify(accountService, times(1)).updateAccountBalance(anyString(), any(BigDecimal.class));
    }
}
