package com.dzurita.msv.clients.model;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @Column(name = "gender", length = 20)
    private String gender;
    @Column(name = "identification", length = 10)
    private String identification;
    @Column(name = "address", length = 75, nullable = false)
    private String address;
    @Column(name = "phone", length = 10, nullable = false)
    private String phone;
}
