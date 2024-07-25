package org.example.clientbank.customer.service;

import org.example.clientbank.account.Account;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.RequestCustomerDto;
import org.example.clientbank.customer.status.CustomerStatus;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    void deleteById(long id);

    List<Customer> findAll();

    Optional<Customer> getCustomerById(long id);

    Customer createCustomer(Customer customer);

    void updateCustomer(Customer customer);

    CustomerStatus updateCustomer(Long id, RequestCustomerDto requestCustomerDto);

    boolean deleteAccountsByCustomerId(long id);

    CustomerStatus deleteAccountByCustomerId(long id, String accountNumber);

    Account createAccountByCustomerId(long id, Currency currency);

    Enum<?> addEmployerToCustomer(long customerId, long employerId);

    Enum<?> removeEmployerFromCustomer(long customerId, long employerId);
}
