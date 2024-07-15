package org.example.clientbank.service;

import org.example.clientbank.dto.CustomerDTO;
import org.example.clientbank.entity.Customer;
import org.example.clientbank.enums.Currency;
import org.example.clientbank.enums.status.CustomerStatus;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    boolean deleteById(long id);

    List<Customer> findAll();

    Optional<Customer> getCustomerById(long id);

    void createCustomer(String name, String email, Integer age);

    void updateCustomer(Customer customer);

    CustomerStatus updateCustomer(Customer customer, CustomerDTO customerDTO);

    boolean deleteAccountsByCustomerId(long id);

    CustomerStatus deleteAccountByCustomerId(long id, String accountNumber);

    boolean createAccountByCustomerId(long id, Currency currency);

    boolean updateCustomerFromDTO(Customer customer, CustomerDTO customerDTO);
}
