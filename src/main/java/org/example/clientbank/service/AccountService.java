package org.example.clientbank.service;

import lombok.AllArgsConstructor;
import org.example.clientbank.dao.CollectionAccountDao;
import org.example.clientbank.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private final CollectionAccountDao collectionAccountDaoDao;

    public Optional<Account> getAccountByAccountNumber(String number) {
        return collectionAccountDaoDao.findByNumber(number);
    }

    public ResponseEntity<String> addFunds(String number, double sum) {
        return getAccountByAccountNumber(number).map(a -> {
            a.setBalance(a.getBalance() + sum);
            return ResponseEntity.ok("Funds add successfully.");
        }).orElse(ResponseEntity.badRequest().body("Account not found."));
    }

    public ResponseEntity<String> withdrawFunds(String number, double sum) {
        return getAccountByAccountNumber(number).map(a -> {
            if (a.getBalance() >= sum) {
                a.setBalance(a.getBalance() - sum);
                return ResponseEntity.ok("Funds withdraw successfully.");
            } else {
                return ResponseEntity.badRequest().body("Insufficient funds.");
            }
        }).orElse(ResponseEntity.badRequest().body("Account not found."));
    }

    public ResponseEntity<String> sendFunds(String numberFrom, String numberTo, double sum) {
        Optional<Account> fromAccountOpt = getAccountByAccountNumber(numberFrom);
        Optional<Account> toAccountOpt = getAccountByAccountNumber(numberTo);

        if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Account not found.");
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (fromAccount.getBalance() >= sum) {
            fromAccount.setBalance(fromAccount.getBalance() - sum);
            toAccount.setBalance(toAccount.getBalance() + sum);
            return ResponseEntity.ok("Funds transferred successfully.");
        } else {
            return ResponseEntity.badRequest().body("Insufficient funds.");
        }

    }
}
