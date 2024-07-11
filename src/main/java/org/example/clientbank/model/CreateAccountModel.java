package org.example.clientbank.model;


import org.example.clientbank.entity.Customer;
import org.example.clientbank.enums.Currency;


public record CreateAccountModel(Customer customer, Currency currency) {
}