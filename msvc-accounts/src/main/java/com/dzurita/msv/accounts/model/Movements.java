package com.dzurita.msv.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "account_number",
            referencedColumnName = "account_number"
    )
    private Account account;

    @Column(name = "movement_date", nullable = false)
    private LocalDate movementDate;

    @Column(nullable = false)
    private Boolean state;

    @Column(name = "movement_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal movementValue;

    @Column(name = "movement_type", nullable = false)
    private String movementType;

    @Column(name = "available_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal availableBalance;

    @Column(name = "initial_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal initialBalance;
}
