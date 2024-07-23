package org.example.clientbank.account.model;

import jakarta.validation.constraints.NotBlank;

public record SendFundsModel(
        @NotBlank(message = "Sender account number cannot be blank")
        String numberFrom,

        @NotBlank(message = "Recipient account number cannot be blank")
        String numberTo,

        @NotBlank(message = "Sum cannot be blank")
        double sum) {
}