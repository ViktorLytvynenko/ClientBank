package org.example.clientbank.account.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddWithdrawFundsModel(
        @NotBlank(message = "Card number cannot be blank")
        String cardNumber,

        @NotNull(message = "Sum cannot be blank")
        double sum) {
}
