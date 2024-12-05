package com.dzurita.msv.accounts.service;

import com.dzurita.msv.accounts.customer.CustomerAccount;
import com.dzurita.msv.accounts.exception.AlreadyExistsException;
import com.dzurita.msv.accounts.exception.NotFoundException;
import com.dzurita.msv.accounts.model.Account;
import com.dzurita.msv.accounts.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerAccount customerAccount;

    public Flux<Account> findAll() {
        return Mono.fromCallable(accountRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Account> findByCustomerIdentification(String identification) {
        return Mono.fromCallable(() -> accountRepository.findByCustomerIdentification(identification))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(existingAccount -> existingAccount
                        .map(Mono::just)
                        .orElseGet(() ->
                                Mono.error(
                                        new NotFoundException("No se encuentran registros de la cuenta")
                                )
                        )
                );
    }

    public Mono<Account> findByAccountNumber(String accountNumber) {
        return Mono.fromCallable(() -> accountRepository.findByAccountNumber(accountNumber))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(existingAccount -> existingAccount
                        .map(Mono::just)
                        .orElseGet(() ->
                                Mono.error(new NotFoundException("No se encuentran registros de la cuenta"))
                        ));
    }

    public Mono<Account> save(Account account) {
        return Mono.fromCallable(() -> accountRepository.findByAccountNumber(account.getAccountNumber()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalAccount -> {
                            if (optionalAccount.isPresent()) {
                                return Mono.error(new AlreadyExistsException("Ya existe la cuenta"));
                            }
                            return fetchCustomerInfoAndSaveAccount(account);
                        }
                );
    }


    public Mono<Account> changeAccountState(String accountNumber, Boolean state) {
        return Mono.fromCallable(() -> accountRepository.findByAccountNumber(accountNumber))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalAccount -> optionalAccount
                        .map(account -> changeStateOfAccount(account, state))
                        .orElseGet(() -> Mono.error(new NotFoundException("No existe usuario para eliminar la cuenta")))
                );
    }

    public Mono<Account> updateAccountBalance(String accountNumber, BigDecimal balance) {
        return Mono.fromCallable(() -> accountRepository.findByAccountNumber(accountNumber))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalAccount -> optionalAccount
                        .map(account -> {
                            account.setBalance(balance);
                            return Mono.fromCallable(() -> accountRepository.save(account))
                                    .subscribeOn(Schedulers.boundedElastic());
                        })
                        .orElseGet(() -> Mono.error(new NotFoundException("No existe usuario para eliminar la cuenta")))
                );
    }

    private Mono<Account> fetchCustomerInfoAndSaveAccount(Account accountRequest) {
        return customerAccount.getCustomerByIdentification(accountRequest.getCustomerIdentification())
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(customerResponse -> {
                    Account account = new Account();
                    account.setAccountNumber(accountRequest.getAccountNumber());
                    account.setAccountType(accountRequest.getAccountType());
                    account.setBalance(accountRequest.getBalance());
                    account.setState(accountRequest.getState());
                    account.setCustomerIdentification(accountRequest.getCustomerIdentification());

                    Account.CustomerInfo customerInfo = new Account.CustomerInfo();
                    customerInfo.setName(customerResponse.getName());
                    customerInfo.setState(customerResponse.getState());
                    account.setCustomerInfo(customerInfo);

                    return Mono.fromCallable(() -> accountRepository.save(account))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    private Mono<Account> changeStateOfAccount(Account account, Boolean state) {
        return Mono.fromCallable(() -> {
                    account.setState(state);
                    return accountRepository.save(account);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

}
