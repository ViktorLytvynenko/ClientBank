package org.example.clientbank.customer.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomerStatus {
    SUCCESS("Success operation."),
    CUSTOMER_NOT_FOUND("Customer not found."),
    EMPLOYER_NOT_FOUND("Employer not found."),
    NOTHING_TO_UPDATE("No changes detected for the customer."),
    CUSTOMER_UPDATED("Customer was successfully updated."),
    DELETED("Customer was successfully deleted."),
    CARD_NOT_FOUND("Card not found"),
    WRONG_CURRENCY("Wrong currency"),
    UNEXPECTED("An unexpected error occurred.");

    private final String message;
}
