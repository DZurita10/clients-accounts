package com.dzurita.msv.clients.service;

import com.dzurita.msv.clients.dto.CustomerStateRequestDTO;
import com.dzurita.msv.clients.exception.AlreadyExistsException;
import com.dzurita.msv.clients.exception.NotFoundException;
import com.dzurita.msv.clients.model.Customer;
import com.dzurita.msv.clients.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    private Customer customer;
    private final String identification = "123456789";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository);

        customer = new Customer();
        customer.setName("Juan");
        customer.setGender("M");
        customer.setIdentification("123456789");
        customer.setAddress("Calle 123");
        customer.setPhone("0987456123");
        customer.setPassword("<PASSWORD>");
        customer.setState(true);
    }

    @Test
    void findAll() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        Flux<Customer> result = customerService.findAll();

        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void findById_customerExists() {
        when(customerRepository.findByIdentification(identification)).thenReturn(Optional.of(customer));

        Mono<Customer> result = customerService.findById(identification);

        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();

        verify(customerRepository, times(1)).findByIdentification(identification);
    }

    @Test
    void findById_customerDoesNotExist() {
        when(customerRepository.findByIdentification(identification)).thenReturn(Optional.empty());

        Mono<Customer> result = customerService.findById(identification);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().equals("El cliente no existe"))
                .verify();

        verify(customerRepository, times(1)).findByIdentification(identification);
    }

    @Test
    void save_customerExists() {
        when(customerRepository.findByIdentification(identification)).thenReturn(Optional.of(customer));

        Mono<Customer> result = customerService.save(customer);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof AlreadyExistsException &&
                                throwable.getMessage().equals("El Cliente ya existe")
                )
                .verify();
    }

    @Test
    void save_customerDoesNotExist() {
        when(customerRepository.findByIdentification(identification)).thenReturn(Optional.empty());
        when(customerRepository.save(customer)).thenReturn(customer);
        Mono<Customer> result = customerService.save(customer);

        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void delete_customerExists() {

        when(customerRepository.findByIdentification(identification)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        customer.setState(false);
        Mono<Customer> result = customerService.update(customer, identification);
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }


    @Test
    void delete_customerDoesNotExist() {
        when(customerRepository.findByIdentification(identification)).thenReturn(Optional.empty());
        CustomerStateRequestDTO customerStateRequestDTO = new CustomerStateRequestDTO();
        customerStateRequestDTO.setState(false);
        Mono<Customer> result = customerService.changeCustomerState(identification, customerStateRequestDTO.getState());
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().equals("El Cliente no existe"))
                .verify();
    }

    @Test
    void update_customerExists() {
        when(customerRepository.findByIdentification(identification)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        Mono<Customer> result = customerService.update(customer, identification);
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void update_customerDoesNotExist() {
        when(customerRepository.findByIdentification(identification)).thenReturn(Optional.empty());
        Mono<Customer> result = customerService.update(customer, identification);
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().equals("No existe cliente"))
                .verify();
    }
}