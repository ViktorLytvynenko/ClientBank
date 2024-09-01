package org.example.clientbank.account.service;

import lombok.AllArgsConstructor;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.db.AccountRepository;
import org.example.clientbank.account.status.AccountStatus;
import org.example.clientbank.exceptions.AccountNotFoundException;
import org.example.clientbank.exceptions.InsufficientBalanceException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Account getAccountByAccountNumber(String number) {
        return accountRepository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(AccountStatus.ACCOUNT_NOT_FOUND.getMessage()));
    }

    @Override
    public Account addFunds(String number, double sum) throws AccountNotFoundException {
        Account account = getAccountByAccountNumber(number);

        account.setBalance(account.getBalance() + sum);
        simpMessagingTemplate.convertAndSend("/topic/balance", account);
        accountRepository.save(account);
        return account;
    }

    @Override
    public Account withdrawFunds(String number, double sum) throws AccountNotFoundException {
        Account account = getAccountByAccountNumber(number);

        if (account.getBalance() >= sum) {
            account.setBalance(account.getBalance() - sum);
            simpMessagingTemplate.convertAndSend("/topic/balance", account);
            accountRepository.save(account);
            return account;
        } else {
            throw new InsufficientBalanceException(AccountStatus.INSUFFICIENT_FUNDS.getMessage());
        }
    }

    @Override
    public Account sendFunds(String numberFrom, String numberTo, double sum) throws AccountNotFoundException {

        Account fromAccount = getAccountByAccountNumber(numberFrom);
        Account toAccount = getAccountByAccountNumber(numberTo);

        if (fromAccount.getBalance() >= sum) {
            fromAccount.setBalance(fromAccount.getBalance() - sum);
            toAccount.setBalance(toAccount.getBalance() + sum);
            simpMessagingTemplate.convertAndSend("/topic/balance", fromAccount);
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            return fromAccount;
        } else {
            throw new InsufficientBalanceException(AccountStatus.INSUFFICIENT_FUNDS.getMessage());
        }
    }
}
