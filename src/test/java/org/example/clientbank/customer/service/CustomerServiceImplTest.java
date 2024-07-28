package org.example.clientbank.customer.service;

import org.example.clientbank.account.Account;
import org.example.clientbank.account.db.AccountRepository;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.RequestCustomerDto;
import org.example.clientbank.customer.db.CustomerRepository;
import org.example.clientbank.customer.status.CustomerStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    private final long johnDoeId = 1L;
    private final long janeDoeId = 2L;
    private final long jackDoeId = 3L;
    private Customer johnDoe;
    private Customer janeDoe;
    private Customer jackDoe;

    @BeforeEach
    void setUp() {
        johnDoe = new Customer("John Doe", "johndoe@gmail.com", 35, "qWerty", "+1234567890");
        johnDoe.setId(johnDoeId);

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
        customerServiceImpl.deleteById(janeDoeId);
        verify(customerRepository, times(1)).deleteById(eq(2L));
    }

    @Test
    void updateCustomer() {
        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));
        when(customerRepository.save(any(Customer.class))).thenReturn(johnDoe);

        RequestCustomerDto requestCustomerDto = new RequestCustomerDto();
        requestCustomerDto.setName("John Smith");
        requestCustomerDto.setEmail("johnsmith@gmail.com");
        requestCustomerDto.setAge(36);
        requestCustomerDto.setPhone("+1234567890");
        requestCustomerDto.setPassword("qWerty");

        CustomerStatus status = customerServiceImpl.updateCustomer(johnDoeId, requestCustomerDto);

        verify(customerRepository, times(1)).findById(johnDoeId);
        verify(customerRepository, times(1)).save(johnDoe);

        assertEquals(CustomerStatus.SUCCESS, status);
        assertNotEquals("John Doe", johnDoe.getName());
        assertNotEquals("johndoe@gmail.com", johnDoe.getEmail());
        assertNotEquals(35, johnDoe.getAge());
    }

    @Test
    void deleteAccountsByCustomerId() {

    }

    @Test
    void deleteAccountByCustomerId() {
    }

    @Test
    @Transactional
    public void testCreateAccountByCustomerId() {
        Currency currency = Currency.USD;

        when(customerRepository.findById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        Account account = customerServiceImpl.createAccountByCustomerId(johnDoeId, currency);

        assertEquals(currency, account.getCurrency());
        assertEquals(johnDoe, account.getCustomer());
        assertEquals(johnDoeId, account.getCustomer().getId());
        assertEquals(1, johnDoe.getAccounts().size());
        verify(accountRepository).save(account);
        verify(customerRepository).save(johnDoe);
    }

    @Test
    void addEmployerToCustomer() {
    }

    @Test
    void removeEmployerFromCustomer() {
    }
}