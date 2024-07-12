package org.example.clientbank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.dao.CollectionCustomerDao;
import org.example.clientbank.entity.Account;
import org.example.clientbank.entity.Customer;
import org.example.clientbank.enums.Currency;
import org.example.clientbank.enums.status.CustomerStatus;
import org.springframework.stereotype.Service;

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

    public List<Customer> findAll() {
        return collectionCustomerDao.findAll();
    }

    public Optional<Customer> getCustomerById(long id) {
        return collectionCustomerDao.getOne(id);
    }

    public Optional<Customer> getCustomerByCustomer(Customer customer) {
        return collectionCustomerDao.getOne(customer);
    }

    public void createCustomer(String name, String email, Integer age) {
        collectionCustomerDao.save(new Customer(name, email, age));
    }

    public CustomerStatus updateCustomer(Customer customer) {
        Optional<Customer> customerOptional = getCustomerById(customer.getId());
        List<Customer> allCustomers = collectionCustomerDao.findAll();

        if (customerOptional.isPresent()) {
            if (!customerOptional.get().getName().equals(customer.getName())
                    || !customerOptional.get().getEmail().equals(customer.getEmail())
                    || !customerOptional.get().getAge().equals(customer.getAge())) {
                int index = allCustomers.indexOf(customerOptional.get());
                allCustomers.set(index, customer);
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

    public boolean deleteAccountsByCustomer(Customer customer) {
        Optional<Customer> customerOptional = getCustomerByCustomer(customer);
        if (customerOptional.isEmpty()) {
            return false;
        }
        customerOptional.get().getAccounts().clear();
        updateCustomer(customerOptional.get());
        return true;
    }

    public CustomerStatus deleteAccountByCustomer(Customer customer, String accountNumber) {
        Optional<Customer> customerOptional = getCustomerByCustomer(customer);
        if (customerOptional.isEmpty()) {
            return CustomerStatus.CUSTOMER_NOT_FOUND;
        }

        boolean removed = customer.getAccounts().removeIf(account -> account.getNumber().equals(accountNumber));
        if (removed) {
            return CustomerStatus.SUCCESS;
        } else {
            return CustomerStatus.CARD_NOT_FOUND;
        }
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

    public boolean createAccountByCustomer(Customer customer, Currency currency) {
        Optional<Customer> customerOptional = getCustomerByCustomer(customer);

        if (customerOptional.isEmpty()) {
            return false;
        }

        customerOptional.get().getAccounts().add(new Account(currency, customer));
        return true;
    }

    public boolean createAccountByCustomerId(long id, Currency currency) {
        Optional<Customer> customerOptional = getCustomerById(id);

        if (customerOptional.isEmpty()) {
            return false;
        }

        customerOptional.get().getAccounts().add(new Account(currency, customerOptional.get()));
        return true;
    }
}
