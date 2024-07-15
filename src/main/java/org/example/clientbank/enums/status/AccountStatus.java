package org.example.clientbank.enums.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountStatus {
    SUCCESS("Success operation."),
    ACCOUNT_NOT_FOUND("Account not found."),
    ACCOUNT_FROM_NOT_FOUND("Account from not found."),
    ACCOUNT_TO_NOT_FOUND("Account to not found."),
    INSUFFICIENT_FUNDS("Insufficient funds.");

    private final String message;
}
