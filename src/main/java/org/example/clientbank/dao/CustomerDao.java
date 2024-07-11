package org.example.clientbank.dao;

import org.example.clientbank.entity.Customer;

import java.util.Optional;

public interface CustomerDao extends Dao<Customer> {
    Optional<Customer> getOne(Customer customer);
}
