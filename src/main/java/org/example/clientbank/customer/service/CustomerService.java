package org.example.clientbank.customer.service;

import org.example.clientbank.account.Account;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.RequestCustomerDto;
import org.example.clientbank.customer.api.dto.RequestPatchCustomerDto;
import org.example.clientbank.customer.status.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    void deleteById(long id);

    List<Customer> findAll();

    Page<Customer> findAllFiltered(Pageable pageable);

    Optional<Customer> getCustomerById(long id);

    Customer createCustomer(Customer customer);

    Optional<Customer> updateCustomer(Long id, RequestCustomerDto requestCustomerDto);

    Optional<Customer> patchCustomer(Long id, RequestPatchCustomerDto requestPatchCustomerDto) throws IllegalAccessException;

    boolean deleteAccountsByCustomerId(long id);

    CustomerStatus deleteAccountByCustomerId(long id, String accountNumber);

    Account createAccountByCustomerId(long id, Currency currency);

    CustomerStatus addEmployerToCustomer(long customerId, long employerId);

    CustomerStatus removeEmployerFromCustomer(long customerId, long employerId);
}
