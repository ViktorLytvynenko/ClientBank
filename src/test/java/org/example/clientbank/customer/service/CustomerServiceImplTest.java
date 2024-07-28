package org.example.clientbank.customer.service;

import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.db.CustomerRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    private final long johnDoeId = 1L;
    private final long janeDoeId = 2L;
    private Customer johnDoe;
    private Customer janeDoe;

    @BeforeEach
    void setUp() {
        johnDoe = new Customer();
        johnDoe.setId(johnDoeId);
        johnDoe.setName("John Doe");
        johnDoe.setAge(35);
        johnDoe.setEmail("johndoe@gmail.com");
        johnDoe.setPassword("qWerty");
        johnDoe.setPhone("+1234567890");

        janeDoe = new Customer();
        janeDoe.setId(janeDoeId);
        janeDoe.setName("Jane Doe");
        janeDoe.setAge(31);
        janeDoe.setEmail("janedoe@gmail.com");
        janeDoe.setPassword("qWerty");
        janeDoe.setPhone("+1234555890");
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
        List<Customer> expectedCustomers = List.of(johnDoe, janeDoe);
        when(customerRepository.findAll()).thenReturn(expectedCustomers);

        List<Customer> actualCustomers = customerServiceImpl.findAll();

        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    void findAllFiltered() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Customer> expectedCustomers = List.of(johnDoe, janeDoe);
        Page<Customer> customerPage = new PageImpl<>(expectedCustomers, pageable, expectedCustomers.size());

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        Page<Customer> result = customerServiceImpl.findAllFiltered(pageable);

        assertEquals(2, result.getTotalElements());
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
    }

    @Test
    void testUpdateCustomer() {
    }

    @Test
    void deleteAccountsByCustomerId() {
    }

    @Test
    void deleteAccountByCustomerId() {
    }

    @Test
    void createAccountByCustomerId() {
    }

    @Test
    void addEmployerToCustomer() {
    }

    @Test
    void removeEmployerFromCustomer() {
    }
}