package org.example.clientbank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.dao.CollectionAccountDao;
import org.example.clientbank.dao.CollectionCustomerDao;
import org.example.clientbank.dto.CustomerDTO;
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
public class CustomerServiceImpl implements CustomerService {

    private final CollectionCustomerDao collectionCustomerDao;
    private final CollectionAccountDao collectionAccountDao;


    @Override
    public boolean deleteById(long id) {
        return collectionCustomerDao.deleteById(id);
    }

    @Override
    public List<Customer> findAll() {
        return collectionCustomerDao.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(long id) {
        return collectionCustomerDao.getOne(id);
    }

    @Override
    public void createCustomer(String name, String email, Integer age) {
        collectionCustomerDao.save(new Customer(name, email, age));
    }

    @Override
    public void updateCustomer(Customer customer) {
        Optional<Customer> customerOptional = getCustomerById(customer.getId());
        List<Customer> allCustomers = collectionCustomerDao.findAll();

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

    @Override
    public CustomerStatus updateCustomer(Customer customer, CustomerDTO customerDTO) {
        Optional<Customer> customerOptional = getCustomerById(customer.getId());
        List<Customer> allCustomers = collectionCustomerDao.findAll();

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


    @Override
    public boolean deleteAccountsByCustomerId(long id) {
        Optional<Customer> customerOptional = getCustomerById(id);
        if (customerOptional.isEmpty()) {
            return false;
        }
        customerOptional.get().getAccounts().clear();
        updateCustomer(customerOptional.get());
        return true;
    }

    @Override
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

    @Override
    public boolean createAccountByCustomerId(long id, Currency currency) {
        Optional<Customer> customerOptional = getCustomerById(id);

        if (customerOptional.isEmpty()) {
            return false;
        }

        Account newAccount = new Account(currency, customerOptional.get());
        customerOptional.get().getAccounts().add(newAccount);
        collectionAccountDao.save(newAccount);
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
