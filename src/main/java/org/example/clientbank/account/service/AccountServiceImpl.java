package org.example.clientbank.account.service;

import lombok.AllArgsConstructor;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.db.AccountRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> getAccountByAccountNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account addFunds(String number, double sum) throws AccountNotFoundException {
        Optional<Account> accountOptional = getAccountByAccountNumber(number);

        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException("Account not found.");
        }

        Account account = accountOptional.get();
        account.setBalance(account.getBalance() + sum);
        accountRepository.save(account);
        return account;
    }

    @Override
    public Account withdrawFunds(String number, double sum) throws AccountNotFoundException {
        Optional<Account> accountOptional = getAccountByAccountNumber(number);

        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException("Account not found.");
        }

        Account account = accountOptional.get();

        if (account.getBalance() >= sum) {
            account.setBalance(account.getBalance() - sum);
            accountRepository.save(account);
            return account;
        } else {
            return null;
        }
    }

    @Override
    public Account sendFunds(String numberFrom, String numberTo, double sum) throws AccountNotFoundException {

        Optional<Account> fromAccountOptional = getAccountByAccountNumber(numberFrom);
        Optional<Account> toAccountOptional = getAccountByAccountNumber(numberTo);

        if (fromAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("Account not found.");
        }
        if (toAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("Account not found.");
        }

        if (fromAccountOptional.get().getBalance() >= sum) {
            fromAccountOptional.get().setBalance(fromAccountOptional.get().getBalance() - sum);
            toAccountOptional.get().setBalance(toAccountOptional.get().getBalance() + sum);
            accountRepository.save(fromAccountOptional.get());
            accountRepository.save(toAccountOptional.get());
            return fromAccountOptional.get();
        } else {
            return null;
        }
    }
}
