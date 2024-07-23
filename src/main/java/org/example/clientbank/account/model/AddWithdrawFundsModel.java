package org.example.clientbank.account.model;

import jakarta.validation.constraints.NotBlank;

public record AddWithdrawFundsModel(
        @NotBlank(message = "Card number cannot be blank")
        String cardNumber,

        @NotBlank(message = "Sum cannot be blank")
        double sum) {
}
