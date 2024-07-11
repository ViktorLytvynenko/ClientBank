package org.example.clientbank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.dao.CollectionCustomerDao;
import org.example.clientbank.entity.Account;
import org.example.clientbank.entity.Customer;
import org.example.clientbank.enums.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService {

    private final CollectionCustomerDao collectionCustomerDao;


    public boolean deleteByCustomer(Customer customer) {
        return collectionCustomerDao.delete(customer);
    }

    public boolean deleteById(long id) {
        return collectionCustomerDao.deleteById(id);
    }

    public void deleteAll(List<Customer> list) {
        collectionCustomerDao.deleteAll(list);
    }

    public void saveAll(List<Customer> list) {
        collectionCustomerDao.saveAll(list);
    }

    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(collectionCustomerDao.findAll());
    }

    public Optional<Customer> getCustomerById(long id) {
        return collectionCustomerDao.getOne(id);
    }

    public Optional<Customer> getCustomerByCustomer(Customer customer) {
        return collectionCustomerDao.getOne(customer);
    }

    public ResponseEntity<String> createCustomer(String name, String email, Integer age) {
        Customer customer = new Customer(name, email, age);
        collectionCustomerDao.save(customer);
        return ResponseEntity.ok("Customer was successfully created.");
    }

    public ResponseEntity<String> updateCustomer(Customer customer) {
        List<Customer> allCustomers = collectionCustomerDao.findAll();

        Optional<Customer> existingCustomer = getCustomerByCustomer(customer);

        if (existingCustomer.isPresent()) {
            Customer c = existingCustomer.get();

            if (!c.equals(customer)) {
                int index = allCustomers.indexOf(c);
                allCustomers.set(index, customer);
                return ResponseEntity.ok("Customer was successfully updated.");
            } else {
                return ResponseEntity.ok("No changes needed.");
            }
        } else {
            return ResponseEntity.badRequest().body("Customer not found.");
        }
    }

    public ResponseEntity<String> deleteAccountsByCustomerId(long id) {
        return getCustomerById(id).map(customer -> {
            customer.getAccounts().clear();
            updateCustomer(customer);
            return ResponseEntity.ok("All accounts were successfully deleted.");
        }).orElse(ResponseEntity.badRequest().body("Customer not found."));
    }

    public ResponseEntity<String> deleteAccountsByCustomer(Customer customer) {
        return getCustomerByCustomer(customer).map(c -> {
            c.getAccounts().clear();
            updateCustomer(customer);
            return ResponseEntity.ok("All accounts were successfully deleted.");
        }).orElse(ResponseEntity.badRequest().body("Customer not found."));
    }

    public ResponseEntity<String> deleteAccountByCustomer(Customer customer, String accountNumber) {
        boolean removed = customer.getAccounts().removeIf(account -> account.getNumber().equals(accountNumber));
        if (removed) {
            return ResponseEntity.ok("Account was successfully deleted.");
        } else {
            return ResponseEntity.badRequest().body("Account not found.");
        }
    }

    public ResponseEntity<String> deleteAccountByCustomerId(long id, String accountNumber) {
        return getCustomerById(id)
                .map(customer -> {
                    boolean removed = customer.getAccounts().removeIf(account -> account.getNumber().equals(accountNumber));
                    if (removed) {
                        return ResponseEntity.ok("Account was successfully deleted.");
                    } else {
                        return ResponseEntity.badRequest().body("Account not found.");
                    }
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Customer not found."));
    }

    public ResponseEntity<String> createAccountByCustomer(Customer customer, Currency currency) {
        return getCustomerByCustomer(customer).map(c -> {
                    c.getAccounts().add(new Account(currency, customer));
                    return ResponseEntity.ok("Customer was successfully created.");
                })
                .orElse(ResponseEntity.badRequest().body("Customer not found."));
    }

    public ResponseEntity<String> createAccountByCustomerId(long id, Currency currency) {
        return getCustomerById(id).map(c -> {
                    c.getAccounts().add(new Account(currency, c));
                    return ResponseEntity.ok("Customer was successfully created.");
                })
                .orElse(ResponseEntity.badRequest().body("Customer not found."));
    }
}
