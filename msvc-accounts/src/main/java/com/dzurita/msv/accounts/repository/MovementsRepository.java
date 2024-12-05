package com.dzurita.msv.accounts.repository;

import com.dzurita.msv.accounts.model.Movements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface MovementsRepository extends JpaRepository<Movements, Integer> {
    List<Movements> findMovementsByAccountAccountNumber(String accountNumber);
    List<Movements> findMovementsByAccountCustomerIdentificationAndMovementDateBetween(
            String identification,
            LocalDate startDate,
            LocalDate endDate
    );
}
