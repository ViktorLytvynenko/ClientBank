package org.example.clientbank.enums.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomerStatus {
    SUCCESS("Success operation."),
    CUSTOMER_NOT_FOUND("Customer not found."),
    NOTHING_TO_UPDATE("No changes detected for the customer."),
    CARD_NOT_FOUND("Card not found"),
    WRONG_CURRENCY("Wrong currency"),
    UNEXPECTED("An unexpected error occurred.");

    private final String message;
}
