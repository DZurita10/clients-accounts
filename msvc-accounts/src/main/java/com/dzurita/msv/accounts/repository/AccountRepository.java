package com.dzurita.msv.accounts.repository;

import com.dzurita.msv.accounts.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByCustomerIdentification(String customerIdentification);
}
