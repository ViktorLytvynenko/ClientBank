package org.example.clientbank.account.service;

import org.example.clientbank.account.Account;
import org.example.clientbank.account.status.AccountStatus;

import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountByAccountNumber(String number);

    AccountStatus addFunds(String number, double sum);

    AccountStatus withdrawFunds(String number, double sum);

    AccountStatus sendFunds(String numberFrom, String numberTo, double sum);
}
