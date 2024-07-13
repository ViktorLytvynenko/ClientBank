package org.example.clientbank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class CustomerServiceImpl implements CustomerService{

    private final CollectionCustomerDao collectionCustomerDao;


    @Override
    public boolean deleteByCustomer(Customer customer) {
        return collectionCustomerDao.delete(customer);
    }

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
    public Optional<Customer> getCustomerByCustomer(Customer customer) {
        return collectionCustomerDao.getOne(customer);
    }

    @Override
    public void createCustomer(String name, String email, Integer age) {
        collectionCustomerDao.save(new Customer(name, email, age));
    }

    @Override
    public CustomerStatus updateCustomer(Customer customer) {
        Optional<Customer> customerOptional = getCustomerById(customer.getId());
        List<Customer> allCustomers = collectionCustomerDao.findAll();

        if (customerOptional.isPresent()) {
            Customer existingCustomer = customerOptional.get();

            existingCustomer.setName(customer.getName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setAge(customer.getAge());

            int index = allCustomers.indexOf(existingCustomer);
            if (index != -1) {
                allCustomers.set(index, existingCustomer);
            }

            return CustomerStatus.SUCCESS;
        } else {
            return CustomerStatus.CUSTOMER_NOT_FOUND;
        }
    }

    @Override
    public CustomerStatus updateCustomer(Customer customer, CustomerDTO customerDTO) {
        Optional<Customer> customerOptional = getCustomerById(customer.getId());
        List<Customer> allCustomers = collectionCustomerDao.findAll();

        if (customerOptional.isPresent()) {
            Customer existingCustomer = customerOptional.get();

            if (!existingCustomer.getName().equals(customerDTO.getName())
                    || !existingCustomer.getEmail().equals(customerDTO.getEmail())
                    || !existingCustomer.getAge().equals(customerDTO.getAge())) {

                existingCustomer.setName(customerDTO.getName());
                existingCustomer.setEmail(customerDTO.getEmail());
                existingCustomer.setAge(customerDTO.getAge());

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
    public boolean deleteAccountsByCustomer(Customer customer) {
        Optional<Customer> customerOptional = getCustomerByCustomer(customer);
        if (customerOptional.isEmpty()) {
            return false;
        }
        customerOptional.get().getAccounts().clear();
        updateCustomer(customerOptional.get());
        return true;
    }

    @Override
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
    public boolean createAccountByCustomer(Customer customer, Currency currency) {
        Optional<Customer> customerOptional = getCustomerByCustomer(customer);

        if (customerOptional.isEmpty()) {
            return false;
        }

        customerOptional.get().getAccounts().add(new Account(currency, customer));
        return true;
    }

    @Override
    public boolean createAccountByCustomerId(long id, Currency currency) {
        Optional<Customer> customerOptional = getCustomerById(id);

        if (customerOptional.isEmpty()) {
            return false;
        }

        customerOptional.get().getAccounts().add(new Account(currency, customerOptional.get()));
        return true;
    }

    @Override
    public void updateCustomerFromDTO(Customer customer, CustomerDTO customerDTO) {
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setAge(customerDTO.getAge());
    }
}
