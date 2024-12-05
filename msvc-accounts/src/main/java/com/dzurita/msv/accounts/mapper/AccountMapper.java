package com.dzurita.msv.accounts.mapper;

import com.dzurita.msv.accounts.dto.AccountRequestDTO;
import com.dzurita.msv.accounts.dto.AccountResponseDTO;
import com.dzurita.msv.accounts.model.Account;

public class AccountMapper {

    public static AccountResponseDTO accountToAccountResponseDTO(Account account) {
        return AccountResponseDTO.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .state(account.getState())
                .customerName(account.getCustomerInfo() != null ? account.getCustomerInfo().getName() : null)
                .build();
    }

    public static Account accountRequestDTOToAccount(AccountRequestDTO accountRequestDTO) {
        Account account = new Account();
        account.setAccountNumber(accountRequestDTO.getAccountNumber());
        account.setAccountType(accountRequestDTO.getAccountType());
        account.setBalance(accountRequestDTO.getBalance());
        account.setState(accountRequestDTO.getState());
        account.setCustomerIdentification(accountRequestDTO.getIdentification());
        account.setCustomerInfo(null);

        return account;
    }
}
