package org.example.clientbank.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.account.db.AccountRepository;
import org.example.clientbank.customer.db.CustomerRepository;
import org.example.clientbank.customer.api.dto.CustomerDTO;
import org.example.clientbank.account.Account;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.status.CustomerStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerServiceImpl{

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;


    public void deleteById(long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(long id) {
        return customerRepository.findById(id);
    }

    public void createCustomer(String name, String email, Integer age) {
        customerRepository.save(new Customer(name, email, age));
    }

    public void updateCustomer(Customer customer) {
        Optional<Customer> customerOptional = getCustomerById(customer.getId());
        List<Customer> allCustomers = customerRepository.findAll();

        if (customerOptional.isPresent()) {
            Customer existingCustomer = customerOptional.get();

            existingCustomer.setName(customer.getName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setAge(customer.getAge());
            existingCustomer.setAccounts(customer.getAccounts());

            int index = allCustomers.indexOf(existingCustomer);
            if (index != -1) {
                allCustomers.set(index, existingCustomer);
            }
        }
    }

    public CustomerStatus updateCustomer(Customer customer, CustomerDTO customerDTO) {
        Optional<Customer> customerOptional = getCustomerById(customer.getId());
        List<Customer> allCustomers = customerRepository.findAll();

        if (customerOptional.isPresent()) {
            Customer existingCustomer = customerOptional.get();

            boolean updated = updateCustomerFromDTO(existingCustomer, customerDTO);

            if (updated) {
                int index = allCustomers.indexOf(existingCustomer);
                if (index != -1) {
                    allCustomers.set(index, existingCustomer);
                }
                return CustomerStatus.SUCCESS;
            } else {
                return CustomerStatus.NOTHING_TO_UPDATE;
            }
        } else {
            return CustomerStatus.CUSTOMER_NOT_FOUND;
        }
    }


    public boolean deleteAccountsByCustomerId(long id) {
        Optional<Customer> customerOptional = getCustomerById(id);
        if (customerOptional.isEmpty()) {
            return false;
        }
        customerOptional.get().getAccounts().clear();
        updateCustomer(customerOptional.get());
        return true;
    }

    public CustomerStatus deleteAccountByCustomerId(long id, String accountNumber) {
        Optional<Customer> customerOptional = getCustomerById(id);

        if (customerOptional.isEmpty()) {
            return CustomerStatus.CUSTOMER_NOT_FOUND;
        }
        boolean removed = customerOptional.get().getAccounts().removeIf(account -> account.getNumber().equals(accountNumber));
        if (removed) {
            return CustomerStatus.SUCCESS;
        } else {
            return CustomerStatus.CARD_NOT_FOUND;
        }
    }

    public boolean createAccountByCustomerId(long id, Currency currency) {
        Optional<Customer> customerOptional = getCustomerById(id);

        if (customerOptional.isEmpty()) {
            return false;
        }

        Account newAccount = new Account(currency, customerOptional.get());
        customerOptional.get().getAccounts().add(newAccount);
        accountRepository.save(newAccount);
        updateCustomer(customerOptional.get());
        return true;
    }

    public boolean updateCustomerFromDTO(Customer customer, CustomerDTO customerDTO) {
        if (!customer.getName().equals(customerDTO.getName())
                || !customer.getEmail().equals(customerDTO.getEmail())
                || !customer.getAge().equals(customerDTO.getAge())) {
            customer.setName(customerDTO.getName());
            customer.setEmail(customerDTO.getEmail());
            customer.setAge(customerDTO.getAge());
            return true;
        }
        return false;
    }
}
