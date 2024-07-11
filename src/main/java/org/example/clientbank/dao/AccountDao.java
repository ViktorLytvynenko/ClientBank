package org.example.clientbank.dao;

import org.example.clientbank.entity.Account;

import java.util.Optional;

public interface AccountDao extends Dao<Account> {
    Optional<Account> findByNumber(String number);
}
