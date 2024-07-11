package org.example.clientbank.model;

import org.example.clientbank.entity.Customer;

public record DeleteAccountModel(Customer customer, String accountNumber) {
}
