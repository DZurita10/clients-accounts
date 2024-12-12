package com.dzurita.msv.accounts.service;

import com.dzurita.msv.accounts.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface IAccountService {
    Flux<Account> findAll();
    Mono<Account> findByCustomerIdentification(String identification);
    Mono<Account> findByAccountNumber(String accountNumber);
    Mono<Account> save(Account account);
    Mono<Account> changeAccountState(String accountNumber, Boolean state);
    Mono<Account> updateAccountBalance(String accountNumber, BigDecimal balance);
}
