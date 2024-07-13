package org.example.clientbank.model;

import org.example.clientbank.enums.Currency;

public record CreateAccountByIdModel(Long id, String currency) {
}