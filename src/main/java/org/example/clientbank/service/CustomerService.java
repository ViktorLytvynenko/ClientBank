package org.example.clientbank.service;

import org.example.clientbank.dto.CustomerDTO;
import org.example.clientbank.entity.Customer;
import org.example.clientbank.enums.Currency;
import org.example.clientbank.enums.status.CustomerStatus;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    boolean deleteByCustomer(Customer customer);

    boolean deleteById(long id);

    List<Customer> findAll();

    Optional<Customer> getCustomerById(long id);

    Optional<Customer> getCustomerByCustomer(Customer customer);

    void createCustomer(String name, String email, Integer age);

    CustomerStatus updateCustomer(Customer customer);

    CustomerStatus updateCustomer(Customer customer, CustomerDTO customerDTO);

    boolean deleteAccountsByCustomerId(long id);

    boolean deleteAccountsByCustomer(Customer customer);

    CustomerStatus deleteAccountByCustomer(Customer customer, String accountNumber);

    CustomerStatus deleteAccountByCustomerId(long id, String accountNumber);

    boolean createAccountByCustomer(Customer customer, Currency currency);

    boolean createAccountByCustomerId(long id, Currency currency);

    void updateCustomerFromDTO(Customer customer, CustomerDTO customerDTO);
}
