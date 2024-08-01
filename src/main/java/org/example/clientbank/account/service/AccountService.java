package org.example.clientbank.account.service;

import org.example.clientbank.account.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountByAccountNumber(String number);

    Account addFunds(String number, double sum) throws AccountNotFoundException;

    Account withdrawFunds(String number, double sum) throws AccountNotFoundException;

    Account sendFunds(String numberFrom, String numberTo, double sum) throws AccountNotFoundException;
}
