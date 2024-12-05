package com.dzurita.msv.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false,  unique = true)
    private String accountNumber;
    @Column(name = "account_type", nullable = false)
    private String accountType;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    @Column(name = "state", nullable = false)
    private Boolean state;
    @Column(name = "customer_identification")
    private String customerIdentification;
    @Embedded
    private CustomerInfo customerInfo;

    @Embeddable
    @Data
    public static class CustomerInfo {
        @Column(name = "customer_name", nullable = false)
        private String name;

        @Column(name = "customer_state", nullable = false)
        private Boolean state;
    }
}
