package com.dzurita.msv.accounts.controller;

import com.dzurita.msv.accounts.dto.AccountRequestDTO;
import com.dzurita.msv.accounts.dto.AccountResponseDTO;
import com.dzurita.msv.accounts.dto.AccountStateRequestDTO;
import com.dzurita.msv.accounts.mapper.AccountMapper;
import com.dzurita.msv.accounts.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@AllArgsConstructor
@Api(tags = "Account Manager")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @ApiOperation(value = "Obtener todas las cuentas", notes = "Este endpoint lista todas las cuentas existentes")
    public Flux<AccountResponseDTO> getAccounts() {
        return accountService.findAll()
                .map(AccountMapper::accountToAccountResponseDTO);
    }

    @GetMapping("/customer_identification/{identification}")
    @ApiOperation(value = "Obtener cuentas de un cliente especifico", notes = "Este endpoint lista las cuentas de un cliente especifico")
    public Mono<AccountResponseDTO> getAccountByIdentification(@PathVariable String identification) {
        return accountService.findByCustomerIdentification(identification)
                .map(AccountMapper::accountToAccountResponseDTO);
    }

    @GetMapping("/account_number/{accountNumber}")
    @ApiOperation(value = "Obtener una cuenta especifica", notes = "Este endpoint nos retorna una cuenta especifica de un cliente")
    public Mono<AccountResponseDTO> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return accountService.findByAccountNumber(accountNumber)
                .map(AccountMapper::accountToAccountResponseDTO);
    }

    @PostMapping
    @ApiOperation(value = "Guardar cuenta", notes = "Este endpoint guarda la cuenta de un cliente")
    public Mono<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO) {
        return Mono.fromCallable(() -> AccountMapper.accountRequestDTOToAccount(accountRequestDTO))
                .flatMap(accountService::save)
                .map(AccountMapper::accountToAccountResponseDTO);
    }

    @PatchMapping("/{accountNumber}/state")
    @ApiOperation(value = "Habilitar / Deshabilitar cuenta", notes = "Este endpoint habilita o deshabilita la cuenta de un cliente ")
    public Mono<AccountResponseDTO> updateStateAccount(@Valid @PathVariable String accountNumber, @RequestBody AccountStateRequestDTO accountStateRequestDTO) {
        return accountService.changeAccountState(accountNumber, accountStateRequestDTO.getState())
                .map(AccountMapper::accountToAccountResponseDTO);
    }
}
