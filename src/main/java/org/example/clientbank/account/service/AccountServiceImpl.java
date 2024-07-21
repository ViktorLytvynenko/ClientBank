package org.example.clientbank.account.service;

import lombok.AllArgsConstructor;
import org.example.clientbank.account.db.AccountRepository;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.status.AccountStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> getAccountByAccountNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public AccountStatus addFunds(String number, double sum) {
        return getAccountByAccountNumber(number).map(a -> {
            a.setBalance(a.getBalance() + sum);
            return AccountStatus.SUCCESS;
        }).orElse(AccountStatus.ACCOUNT_NOT_FOUND);
    }

    @Override
    public AccountStatus withdrawFunds(String number, double sum) {
        return getAccountByAccountNumber(number).map(a -> {
            if (a.getBalance() >= sum) {
                a.setBalance(a.getBalance() - sum);
                return AccountStatus.SUCCESS;
            } else {
                return AccountStatus.INSUFFICIENT_FUNDS;
            }
        }).orElse(AccountStatus.ACCOUNT_NOT_FOUND);
    }

    @Override
    public AccountStatus sendFunds(String numberFrom, String numberTo, double sum) {

        Optional<Account> fromAccountOptional = getAccountByAccountNumber(numberFrom);
        Optional<Account> toAccountOptional = getAccountByAccountNumber(numberTo);
        if (fromAccountOptional.isEmpty()) {
            return AccountStatus.ACCOUNT_FROM_NOT_FOUND;
        }
        if (toAccountOptional.isEmpty()) {
            return AccountStatus.ACCOUNT_TO_NOT_FOUND;
        }

        if (fromAccountOptional.get().getBalance() >= sum) {
            fromAccountOptional.get().setBalance(fromAccountOptional.get().getBalance() - sum);
            toAccountOptional.get().setBalance(toAccountOptional.get().getBalance() + sum);
            return AccountStatus.SUCCESS;
        } else {
            return AccountStatus.INSUFFICIENT_FUNDS;
        }
    }
}
