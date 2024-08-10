package org.example.clientbank.customer.service;

import lombok.RequiredArgsConstructor;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.db.AccountRepository;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.RequestCustomerDto;
import org.example.clientbank.customer.api.dto.RequestPatchCustomerDto;
import org.example.clientbank.customer.db.CustomerRepository;
import org.example.clientbank.customer.status.CustomerStatus;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.db.EmployerRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final EmployerRepository employerRepository;


    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Page<Customer> findAllFiltered(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Optional<Customer> getCustomerById(long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteById(long id) {
        if (!customerRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Customer not found with id: " + id, 1);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<Customer> updateCustomer(Long id, RequestCustomerDto requestCustomerDto) {
        Optional<Customer> customerOptional = getCustomerById(id);
        if (customerOptional.isEmpty()) {
            return Optional.empty();
        }

        customerOptional.get().setName(requestCustomerDto.getName());
        customerOptional.get().setEmail(requestCustomerDto.getEmail());
        customerOptional.get().setAge(requestCustomerDto.getAge());

        customerRepository.save(customerOptional.get());
        return customerOptional;
    }

    @Override
    public Optional<Customer> patchCustomer(Long id, RequestPatchCustomerDto requestPatchCustomerDto) throws IllegalAccessException {
        Optional<Customer> customerOptional = getCustomerById(id);
        if (customerOptional.isEmpty()) {
            return Optional.empty();
        }

        Field[] dtoFields = RequestPatchCustomerDto.class.getDeclaredFields();
        Field[] entityFields = Customer.class.getDeclaredFields();

        for (Field dtoField : dtoFields) {
            dtoField.setAccessible(true);
            Object value = dtoField.get(requestPatchCustomerDto);
            if (value != null) {
                String fieldName = dtoField.getName();

                for (Field entityField : entityFields) {
                    if (entityField.getName().equals(fieldName) && entityField.getType().equals(dtoField.getType())) {
                        entityField.setAccessible(true);
                        entityField.set(customerOptional.get(), value);
                        entityField.setAccessible(false);
                        break;
                    }
                }
            }
            dtoField.setAccessible(false);
        }

        customerRepository.save(customerOptional.get());
        return customerOptional;
    }

    @Override
    @Transactional
    public boolean deleteAccountsByCustomerId(long id) {
        Optional<Customer> customerOptional = getCustomerById(id);
        if (customerOptional.isEmpty()) {
            return false;
        }

        Customer customer = customerOptional.get();
        List<Account> accounts = customer.getAccounts();

        if (!accounts.isEmpty()) {
            accountRepository.deleteAll(accounts);
        }

        customer.getAccounts().clear();
        customerRepository.save(customer);
        return true;
    }

    @Override
    @Transactional
    public CustomerStatus deleteAccountByCustomerId(long id, String accountNumber) {
        Optional<Customer> customerOptional = getCustomerById(id);

        if (customerOptional.isEmpty()) {
            return CustomerStatus.CUSTOMER_NOT_FOUND;
        }

        Customer customer = customerOptional.get();

        boolean removed = customer.getAccounts().removeIf(account -> account.getNumber().equals(accountNumber));
        if (removed) {
            accountRepository.deleteByNumber(accountNumber);
            customerRepository.save(customer);
            return CustomerStatus.SUCCESS;
        } else {
            return CustomerStatus.CARD_NOT_FOUND;
        }
    }

    @Override
    @Transactional
    public Account createAccountByCustomerId(long id, Currency currency) {
        Optional<Customer> customerOptional = getCustomerById(id);

        if (customerOptional.isEmpty()) {
            return null;
        }

        Customer customer = customerOptional.get();

        Account newAccount = new Account(currency, customer);
        customer.getAccounts().add(newAccount);

        customerRepository.save(customer);
        return newAccount;
    }

    @Override
    @Transactional
    public CustomerStatus addEmployerToCustomer(long customerId, long employerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        Optional<Employer> employerOptional = employerRepository.findById(employerId);

        if (customerOptional.isEmpty()) {
            return CustomerStatus.EMPLOYER_NOT_FOUND;
        }

        if (employerOptional.isEmpty()) {
            return CustomerStatus.UNEXPECTED;
        }

        Customer customer = customerOptional.get();
        Employer employer = employerOptional.get();

        boolean customerUpdated = false;
        boolean employerUpdated = false;

        if (!customer.getEmployers().contains(employer)) {
            customer.getEmployers().add(employer);
            customerUpdated = true;
        }

        if (!employer.getCustomers().contains(customer)) {
            employer.getCustomers().add(customer);
            employerUpdated = true;
        }

        if (!customerUpdated && !employerUpdated) {
            return CustomerStatus.NOTHING_TO_UPDATE;
        }

        customerRepository.save(customer);

        return CustomerStatus.SUCCESS;
    }

    @Override
    @Transactional
    public CustomerStatus removeEmployerFromCustomer(long customerId, long employerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        Optional<Employer> employerOptional = employerRepository.findById(employerId);

        if (customerOptional.isEmpty()) {
            return CustomerStatus.CUSTOMER_NOT_FOUND;
        }

        if (employerOptional.isEmpty()) {
            return CustomerStatus.EMPLOYER_NOT_FOUND;
        }

        Customer customer = customerOptional.get();
        Employer employer = employerOptional.get();

        boolean customerUpdated = false;
        boolean employerUpdated = false;

        if (customer.getEmployers().contains(employer)) {
            customer.getEmployers().remove(employer);
            customerUpdated = true;
        }

        if (employer.getCustomers().contains(customer)) {
            employer.getCustomers().remove(customer);
            employerUpdated = true;
        }

        if (!customerUpdated && !employerUpdated) {
            return CustomerStatus.NOTHING_TO_UPDATE;
        }

        customerRepository.save(customer);

        return CustomerStatus.SUCCESS;
    }
}
