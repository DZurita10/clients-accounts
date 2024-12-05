package com.dzurita.msv.accounts.service;

import com.dzurita.msv.accounts.customer.CustomerAccount;
import com.dzurita.msv.accounts.dto.CustomerResponseDTO;
import com.dzurita.msv.accounts.exception.AlreadyExistsException;
import com.dzurita.msv.accounts.model.Account;
import com.dzurita.msv.accounts.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerAccount customerAccount;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNewAccount() {

        Account account = new Account();
        account.setAccountNumber("12345");
        account.setCustomerIdentification("98765");
        account.setBalance(new BigDecimal("1000.00"));
        account.setAccountType("Savings");
        account.setState(true);

        Account.CustomerInfo newAccount = new Account.CustomerInfo();
        newAccount.setName("Diego");
        newAccount.setState(true);

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());
        when(customerAccount.getCustomerByIdentification("98765"))
                .thenReturn(Mono.just(new CustomerResponseDTO()));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mono<Account> result = accountService.save(account);

        StepVerifier.create(result)
                .expectNextMatches(savedAccount -> savedAccount.getAccountNumber().equals("12345") && savedAccount.getCustomerIdentification().equals("98765"))
                .verifyComplete();
    }

    @Test
    void testSaveExistingAccount() {
        Account account = new Account();
        account.setAccountNumber("12345");

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        Mono<Account> result = accountService.save(account);

        StepVerifier.create(result)
                .expectError(AlreadyExistsException.class)
                .verify();
    }

    @Test
    void testSaveAccountWithCustomerInfoError() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setCustomerIdentification("98765");

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());
        when(customerAccount.getCustomerByIdentification("98765"))
                .thenReturn(Mono.error(new RuntimeException("Customer service error")));

        Mono<Account> result = accountService.save(account);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
