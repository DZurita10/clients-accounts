package com.dzurita.msv.accounts.mapper;


import com.dzurita.msv.accounts.dto.MovementsRegisterRequestDTO;
import com.dzurita.msv.accounts.dto.MovementsReportResponseDTO;
import com.dzurita.msv.accounts.dto.MovementsResponseDTO;
import com.dzurita.msv.accounts.model.Account;
import com.dzurita.msv.accounts.model.Movements;

import java.util.Date;

public class MovementMapper {
    public static MovementsResponseDTO movementToMovementsResponseDTO(Movements movements){
        return MovementsResponseDTO.builder()
                .movementDate(movements.getMovementDate())
                .accountNumber(movements.getAccount().getAccountNumber())
                .movementValue(movements.getMovementValue())
                .movementType(movements.getMovementType())
                .state(movements.getState())
                .initialBalance(movements.getInitialBalance())
                .availableBalance(movements.getAvailableBalance())
                .build();
    }

    public static Movements movementRequestDTOToMovements(MovementsRegisterRequestDTO movementsRegisterRequestDTO){
        Account account = new Account();
        account.setAccountNumber(movementsRegisterRequestDTO.getAccountNumber());
        Movements movements = new Movements();
        movements.setMovementType(movementsRegisterRequestDTO.getMovementType());
        movements.setMovementValue(movementsRegisterRequestDTO.getMovementValue());
        movements.setAccount(account);
        return movements;
    }

    public static MovementsReportResponseDTO movementsToMovementsReportResponseDTO(Movements movements){
        return MovementsReportResponseDTO.builder()
                .date(movements.getMovementDate())
                .customerName(movements.getAccount().getCustomerInfo().getName())
                .accountNumber(movements.getAccount().getAccountNumber())
                .accountType(movements.getAccount().getAccountType())
                .initialValue(movements.getInitialBalance())
                .status(movements.getState())
                .movementValue(movements.getMovementValue())
                .balance(movements.getAvailableBalance())
                .movementType(movements.getMovementType())
                .build();
    }
}
