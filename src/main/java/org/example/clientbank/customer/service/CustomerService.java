package org.example.clientbank.customer.service;

import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.CustomerDTO;
import org.example.clientbank.customer.status.CustomerStatus;
import org.example.clientbank.account.enums.Currency;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    void deleteById(long id);

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
