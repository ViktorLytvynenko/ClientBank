package org.example.clientbank.account.service;

import org.example.clientbank.account.Account;
import org.example.clientbank.account.db.AccountRepository;
import org.example.clientbank.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

import static org.example.clientbank.account.enums.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;


    private Account firstAccount;
    private Account secondAccount;

    @BeforeEach
    void setUp() {
        Customer johnDoe = new Customer("John Doe", "johndoe@gmail.com", 35, "qWerty", "+1234567890");
        johnDoe.setId(1L);

        firstAccount = new Account();
        firstAccount.setId(1L);
        firstAccount.setCurrency(USD);
        firstAccount.setCustomer(johnDoe);
        firstAccount.setNumber("123456789");
        firstAccount.setBalance(100.0);

        secondAccount = new Account();
        secondAccount.setId(2L);
        secondAccount.setCurrency(USD);
        secondAccount.setCustomer(johnDoe);
        secondAccount.setNumber("555555555");
        secondAccount.setBalance(75.0);
    }

    @Test
    void getAccountByAccountNumber() {
        when(accountRepository.findByNumber("123456789")).thenReturn(Optional.of(firstAccount));

        Optional<Account> accountOptional = accountServiceImpl.getAccountByAccountNumber("123456789");

        assertTrue(accountOptional.isPresent());
        assertEquals(firstAccount.getNumber(), accountOptional.get().getNumber());
    }

    @Test
    void addFunds() throws AccountNotFoundException {
        when(accountRepository.findByNumber("123456789")).thenReturn(Optional.of(firstAccount));

        Account updatedAccount = accountServiceImpl.addFunds("123456789", 50.0);

        assertEquals(150.0, updatedAccount.getBalance());
        verify(accountRepository).save(firstAccount);
    }

    @Test
    void withdrawFunds() throws AccountNotFoundException {
        when(accountRepository.findByNumber("123456789")).thenReturn(Optional.of(firstAccount));

        Account updatedAccount = accountServiceImpl.withdrawFunds("123456789", 50.0);

        assertEquals(50.0, updatedAccount.getBalance());
        verify(accountRepository).save(firstAccount);
    }

    @Test
    void sendFunds() throws AccountNotFoundException {
        when(accountRepository.findByNumber("123456789")).thenReturn(Optional.of(firstAccount));
        when(accountRepository.findByNumber("555555555")).thenReturn(Optional.of(secondAccount));

        Account updatedFromAccount = accountServiceImpl.sendFunds("123456789", "555555555", 15.0);

        assertEquals(85.0, updatedFromAccount.getBalance());
        verify(accountRepository).save(firstAccount);
        verify(accountRepository).save(secondAccount);
    }
}