package org.example.clientbank.dao;

import org.example.clientbank.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CollectionAccountDao implements AccountDao {

    private final List<Account> accounts;
    private long currentId = 1;

    public CollectionAccountDao() {
        this.accounts = new ArrayList<>();
    }

    @Override
    public void save(Account account) {
        account.setId(currentId);
        currentId++;
        accounts.add(account);
    }

    @Override
    public boolean delete(Account account) {
        return accounts.remove(account);
    }

    @Override
    public void deleteAll(List<Account> list) {
        accounts.removeAll(list);
    }

    @Override
    public void saveAll(List<Account> list) {
        for (Account account : list) {
            save(account);
        }
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts);
    }

    @Override
    public boolean deleteById(long id) {
        if (!accounts.isEmpty()) {
            return accounts.removeIf(account -> account.getId().equals(id));
        }
        return false;
    }

    @Override
    public Optional<Account> getOne(long id) {
        return accounts.stream().filter(account -> account.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        return accounts.stream().filter(account -> account.getNumber().equals(number))
                .findFirst();
    }
}
