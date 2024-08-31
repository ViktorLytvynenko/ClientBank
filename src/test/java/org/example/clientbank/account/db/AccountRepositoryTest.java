package org.example.clientbank.account.db;

import org.example.clientbank.account.Account;
import org.example.clientbank.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.clientbank.account.enums.Currency.USD;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void whenSaveCustomer_thenFindById() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);
        customer.setPhone("1234567890");
        customer.setPassword("password");

        Account account = new Account();
        account.setNumber("123456789");
        account.setCurrency(USD);
        account.setBalance(100.0);
        account.setCustomer(customer);

        Account savedAccount = accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getNumber()).isEqualTo("123456789");
    }

    @Test
    void findByNumber() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);
        customer.setPhone("1234567890");
        customer.setPassword("password");

        Account account = new Account();
        account.setNumber("123456789");
        account.setCurrency(USD);
        account.setBalance(100.0);
        account.setCustomer(customer);

        accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findByNumber("123456789");
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getNumber()).isEqualTo("123456789");
    }

    @Test
    void deleteByNumber() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);
        customer.setPhone("1234567890");
        customer.setPassword("password");

        Account account = new Account();
        account.setNumber("123456789");
        account.setCurrency(USD);
        account.setBalance(100.0);
        account.setCustomer(customer);

        accountRepository.save(account);

        accountRepository.deleteByNumber("123456789");

        Optional<Account> foundAccount = accountRepository.findByNumber("123456789");
        assertThat(foundAccount).isEmpty();
    }
}