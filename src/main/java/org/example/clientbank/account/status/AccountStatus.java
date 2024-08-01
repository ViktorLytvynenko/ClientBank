package org.example.clientbank.account.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountStatus {
    SUCCESS("Success operation."),
    ACCOUNT_CREATED("Account was created."),
    ACCOUNT_DELETED("Account was deleted."),
    ACCOUNTS_DELETED("Accounts was deleted."),
    ACCOUNT_NOT_FOUND("Account not found."),
    ACCOUNT_FROM_NOT_FOUND("Account from not found."),
    ACCOUNT_TO_NOT_FOUND("Account to not found."),
    INSUFFICIENT_FUNDS("Insufficient funds."),
    ADDED_FUNDS("Funds add successfully."),
    WITHDRAW_FUNDS("Funds withdraw successfully."),
    SEND_FUNDS("Funds send successfully."),
    UNEXPECTED("An unexpected error occurred.");

    private final String message;
}
