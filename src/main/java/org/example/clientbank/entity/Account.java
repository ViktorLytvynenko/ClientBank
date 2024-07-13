package org.example.clientbank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.enums.Currency;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
@Data
public class Account extends AbstractEntity {
    Long id;
    final String number;
    Currency currency;
    Double balance;
    //уточнить на счет этого!
    @JsonIgnore
    Customer customer;

    public Account(Currency currency, Customer customer) {
        this.id = null;
        this.number = UUID.randomUUID().toString();
        this.currency = currency;
        this.balance = 0.0;
        this.customer = customer;
    }
}
