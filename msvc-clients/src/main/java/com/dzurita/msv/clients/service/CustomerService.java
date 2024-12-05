package com.dzurita.msv.clients.service;

import com.dzurita.msv.clients.exception.AlreadyExistsException;
import com.dzurita.msv.clients.exception.NotFoundException;
import com.dzurita.msv.clients.model.Customer;
import com.dzurita.msv.clients.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Flux<Customer> findAll() {
        return Mono.fromCallable(customerRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Customer> findById(String id) {
        return Mono.fromCallable(() -> customerRepository.findByIdentification(id))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(optionalCustomer ->
                        optionalCustomer.map(Mono::just)
                                .orElse(Mono.error(new NotFoundException("El cliente no existe")))
                );
    }

    public Mono<Customer> save(Customer customer) {
        return Mono.fromCallable(() -> customerRepository.findByIdentification(customer.getIdentification()))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(optionalCustomer -> optionalCustomer
                        .map(existingCustomer ->
                                Mono.<Customer>error(new AlreadyExistsException("El Cliente ya existe"))
                        )
                        .orElseGet(() -> Mono.fromCallable(() -> customerRepository.save(customer))
                                .publishOn(Schedulers.boundedElastic()))
                );
    }

    public Mono<Customer> changeCustomerState(String id, Boolean state) {
        return Mono.fromCallable(() -> customerRepository.findByIdentification(id))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(optionalCustomer -> optionalCustomer
                        .map(customer -> changeStateOfCustomer(customer, state)
                        )
                        .orElseGet(() -> Mono.error(new NotFoundException("El Cliente no existe")))
                );
    }

    public Mono<Customer> update(Customer customer, String identification) {
        return Mono.fromCallable(() -> customerRepository.findByIdentification(identification))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(optionalCustomer -> optionalCustomer
                        .map(existingCustomer -> updateCustomer(customer, existingCustomer.getId())
                        )
                        .orElseGet(() -> Mono.error(new NotFoundException("No existe cliente")))
                );
    }

    private Mono<Customer> changeStateOfCustomer(Customer customer, Boolean state) {
        return Mono.fromCallable(() -> {
                    customer.setState(state);
                    return customerRepository.save(customer);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Customer> updateCustomer(Customer customer, Long id) {
        customer.setId(id);
        return Mono.fromCallable(() -> customerRepository.save(customer))
                .publishOn(Schedulers.boundedElastic());

    }
}
