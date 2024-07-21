package org.example.clientbank.account.api.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.clientbank.account.enums.Currency;

public class AccountDto {

    @NotBlank(message = "Number must not be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String number;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Currency must not be blank")
    @Size(max = 3, message = "Currency must not exceed 3 characters")
    Currency currency;

    @NotBlank(message = "Balance must not be blank")
    @Size(max = 15, message = "Currency must not exceed 15 characters")
    Double balance;
}
