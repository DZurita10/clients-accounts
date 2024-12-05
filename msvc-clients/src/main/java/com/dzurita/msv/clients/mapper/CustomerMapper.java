package com.dzurita.msv.clients.mapper;

import com.dzurita.msv.clients.dto.CustomerRequestDTO;
import com.dzurita.msv.clients.model.Customer;
import com.dzurita.msv.clients.dto.CustomerResponseDTO;

public class CustomerMapper {

    public static CustomerResponseDTO toCustomerResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .name(customer.getName())
                .gender(customer.getGender())
                .identification(customer.getIdentification())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .state(customer.getState())
                .build();
    }

    public static Customer toCustomer(CustomerRequestDTO customerRequestDTO){
        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setGender(customerRequestDTO.getGender());
        customer.setIdentification(customerRequestDTO.getIdentification());
        customer.setAddress(customerRequestDTO.getAddress());
        customer.setPhone(customerRequestDTO.getPhone());
        customer.setPassword(customerRequestDTO.getPassword());
        customer.setState(customerRequestDTO.getState());
        return customer;
    }
}
