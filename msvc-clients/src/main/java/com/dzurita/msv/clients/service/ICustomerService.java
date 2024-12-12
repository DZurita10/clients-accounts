package com.dzurita.msv.clients.service;

import com.dzurita.msv.clients.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {
    Flux<Customer> findAll();
    Mono<Customer> findById(String id);
    Mono<Customer> save(Customer customer);
    Mono<Customer> changeCustomerState(String id, Boolean state);
    Mono<Customer> update(Customer customer, String identification);
}
