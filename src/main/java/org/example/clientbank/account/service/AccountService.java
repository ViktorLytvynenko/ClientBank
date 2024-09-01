package org.example.clientbank.account.service;

import org.example.clientbank.account.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountByAccountNumber(String number);

    Account addFunds(String number, double sum);

    Account withdrawFunds(String number, double sum);

    Account sendFunds(String numberFrom, String numberTo, double sum);
}
