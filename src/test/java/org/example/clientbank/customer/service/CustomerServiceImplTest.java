package org.example.clientbank.customer.service;

import org.example.clientbank.account.Account;
import org.example.clientbank.account.db.AccountRepository;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.RequestCustomerDto;
import org.example.clientbank.customer.api.dto.RequestPatchCustomerDto;
import org.example.clientbank.customer.db.CustomerRepository;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.db.EmployerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.example.clientbank.account.enums.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EmployerRepository employerRepository;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;


    private final long johnDoeId = 1L;
    private final long janeDoeId = 2L;
    private final long jackDoeId = 3L;
    private Customer johnDoe;
    private Customer janeDoe;
    private Customer jackDoe;

    private final long googleId = 1L;
    private final long amazonId = 2L;
    private Employer google;
    private Employer amazon;

    @BeforeEach
    void setUp() {
        google = new Employer("Google", "USA, California");
        google.setId(googleId);

        amazon = new Employer("Amazon", "USA, Nevada");
        amazon.setId(amazonId);

        johnDoe = new Customer("John Doe", "johndoe@gmail.com", 35, "qWerty", "+1234567890");
        johnDoe.setId(johnDoeId);
        johnDoe.getAccounts().add(new Account(USD, johnDoe));
        johnDoe.getEmployers().add(google);
        johnDoe.getEmployers().add(amazon);

        janeDoe = new Customer("Jane Doe", "janedoe@gmail.com", 31, "qWerty", "+1234555890");
        janeDoe.setId(janeDoeId);

        jackDoe = new Customer("Jack Doe", "jackdoe@gmail.com", 30, "qWerty", "+1235555890");
        jackDoe.setId(jackDoeId);
    }


    @Test
    void getCustomerById() {
        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        Optional<Customer> actualCustomer = customerServiceImpl.getCustomerById(johnDoeId);

        assertTrue(actualCustomer.isPresent());
        assertEquals(johnDoe, actualCustomer.get());
    }

    @Test
    void findAll() {
        List<Customer> expectedCustomers = List.of(johnDoe, janeDoe, jackDoe);
        when(customerRepository.findAll()).thenReturn(expectedCustomers);

        List<Customer> actualCustomers = customerServiceImpl.findAll();

        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    void findAllFiltered() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Customer> expectedCustomers = List.of(johnDoe, janeDoe, jackDoe);
        Page<Customer> customerPage = new PageImpl<>(expectedCustomers, pageable, expectedCustomers.size());

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        Page<Customer> result = customerServiceImpl.findAllFiltered(pageable);

        assertEquals(3, result.getTotalElements());
        assertEquals(expectedCustomers, result.getContent());
    }

    @Test
    void createCustomer() {
        when(customerRepository.save(johnDoe)).thenReturn(johnDoe);
        Customer savedCustomer = customerServiceImpl.createCustomer(johnDoe);

        assertEquals(johnDoe, savedCustomer);
    }

    @Test
    void deleteById() {
        when(customerRepository.existsById(janeDoeId)).thenReturn(true);
        customerServiceImpl.deleteById(janeDoeId);

        verify(customerRepository, times(1)).deleteById(janeDoeId);
    }

    @Test
    void updateCustomer() {
        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        RequestCustomerDto requestCustomerDto = new RequestCustomerDto("Max", "max@gmail.com", 25, "+3334567890", "newPassword123!");

        Optional<Customer> updatedCustomerOptional = customerServiceImpl.updateCustomer(johnDoeId, requestCustomerDto);
        assertTrue(updatedCustomerOptional.isPresent());

        Customer updatedCustomer = updatedCustomerOptional.get();

        assertNotEquals(johnDoe.getName(), "John Doe");
        assertEquals(johnDoe.getName(), updatedCustomer.getName());

        assertNotEquals(johnDoe.getEmail(), "johndoe@gmail.com");
        assertEquals(johnDoe.getEmail(), updatedCustomer.getEmail());
    }

    @Test
    void patchCustomer() throws IllegalAccessException {
        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        RequestPatchCustomerDto requestPatchCustomerDto = new RequestPatchCustomerDto("Max", "max@gmail.com", 25, "+3334567890");

        Optional<Customer> updatedCustomerOptional = customerServiceImpl.patchCustomer(johnDoeId, requestPatchCustomerDto);
        assertTrue(updatedCustomerOptional.isPresent());

        Customer updatedCustomer = updatedCustomerOptional.get();

        assertNotEquals(johnDoe.getName(), "John Doe");
        assertEquals(johnDoe.getName(), updatedCustomer.getName());

        assertNotEquals(johnDoe.getEmail(), "johndoe@gmail.com");
        assertEquals(johnDoe.getEmail(), updatedCustomer.getEmail());
    }

    @Test
    void deleteAccountsByCustomerId() {
        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        customerServiceImpl.deleteAccountsByCustomerId(johnDoeId);
        assertEquals(johnDoe.getAccounts().size(), 0);
    }

    @Test
    void deleteAccountByCustomerId() {
        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        customerServiceImpl.deleteAccountByCustomerId(johnDoeId, johnDoe.getAccounts().getFirst().getNumber());
        assertEquals(johnDoe.getAccounts().size(), 0);
    }

    @Test
    @Transactional
    public void testCreateAccountByCustomerId() {
        Currency currency = USD;

        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        Account account = customerServiceImpl.createAccountByCustomerId(johnDoeId, currency);

        assertEquals(currency, account.getCurrency());
        assertEquals(johnDoe, account.getCustomer());
        assertEquals(johnDoeId, account.getCustomer().getId());
        assertEquals(2, johnDoe.getAccounts().size());
        verify(customerRepository).save(johnDoe);
    }

    @Test
    void addEmployerToCustomer() {
        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        Employer newEmployer = new Employer("Bosch", "Germany");
        newEmployer.setId(3L);

        when(employerRepository.findById(3L)).thenReturn(Optional.of(newEmployer));

        customerServiceImpl.addEmployerToCustomer(johnDoeId, newEmployer.getId());

        verify(customerRepository).save(johnDoe);
        assertTrue(johnDoe.getEmployers().contains(newEmployer));
        assertEquals(johnDoe.getEmployers().size(), 3);
    }

    @Test
    void removeEmployerFromCustomer() {
        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));
        when(employerRepository.findById(amazonId)).thenReturn(Optional.of(amazon));

        customerServiceImpl.removeEmployerFromCustomer(johnDoeId, amazonId);
        assertEquals(johnDoe.getEmployers().size(), 1);

        verify(customerRepository, times(1)).save(johnDoe);
    }
}