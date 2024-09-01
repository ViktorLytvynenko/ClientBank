package org.example.clientbank.account.service;

import lombok.AllArgsConstructor;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.db.AccountRepository;
import org.example.clientbank.account.status.AccountStatus;
import org.example.clientbank.exceptions.InsufficientBalanceException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.example.clientbank.exceptions.AccountNotFoundException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Optional<Account> getAccountByAccountNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account addFunds(String number, double sum) throws AccountNotFoundException {
        Optional<Account> accountOptional = getAccountByAccountNumber(number);

        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException(AccountStatus.ACCOUNT_NOT_FOUND.getMessage());
        }

        Account account = accountOptional.get();
        account.setBalance(account.getBalance() + sum);
        simpMessagingTemplate.convertAndSend("/topic/balance", account);
        accountRepository.save(account);
        return account;
    }

    @Override
    public Account withdrawFunds(String number, double sum) throws AccountNotFoundException {
        Optional<Account> accountOptional = getAccountByAccountNumber(number);

        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException(AccountStatus.ACCOUNT_NOT_FOUND.getMessage());
        }

        Account account = accountOptional.get();

        if (account.getBalance() >= sum) {
            account.setBalance(account.getBalance() - sum);
            accountRepository.save(account);
            return account;
        } else {
            throw new InsufficientBalanceException(AccountStatus.INSUFFICIENT_FUNDS.getMessage());
        }
    }

    @Override
    public Account sendFunds(String numberFrom, String numberTo, double sum) throws AccountNotFoundException {

        Optional<Account> fromAccountOptional = getAccountByAccountNumber(numberFrom);
        Optional<Account> toAccountOptional = getAccountByAccountNumber(numberTo);

        if (fromAccountOptional.isEmpty()) {
            throw new AccountNotFoundException(AccountStatus.ACCOUNT_NOT_FOUND.getMessage());
        }
        if (toAccountOptional.isEmpty()) {
            throw new AccountNotFoundException(AccountStatus.ACCOUNT_NOT_FOUND.getMessage());
        }

        if (fromAccountOptional.get().getBalance() >= sum) {
            fromAccountOptional.get().setBalance(fromAccountOptional.get().getBalance() - sum);
            toAccountOptional.get().setBalance(toAccountOptional.get().getBalance() + sum);
            accountRepository.save(fromAccountOptional.get());
            accountRepository.save(toAccountOptional.get());
            return fromAccountOptional.get();
        } else {
            throw new InsufficientBalanceException(AccountStatus.INSUFFICIENT_FUNDS.getMessage());
        }
    }
}
