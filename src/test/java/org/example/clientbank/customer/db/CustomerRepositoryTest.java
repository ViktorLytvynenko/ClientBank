package org.example.clientbank.customer.db;

import org.example.clientbank.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void whenSaveCustomer_thenFindById() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);
        customer.setPhone("1234567890");
        customer.setPassword("password");

        Customer savedCustomer = customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getName()).isEqualTo("John Doe");
    }
}