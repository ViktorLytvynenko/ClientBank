package org.example.clientbank.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.AbstractEntity;
import org.example.clientbank.account.enums.Currency;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "accounts")
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
@Data
@NoArgsConstructor
@ToString(exclude = "customer")
public class Account extends AbstractEntity {

    @Column(nullable = false)
    String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Currency currency;

    @Column(nullable = false)
    Double balance;

    @ManyToOne
    @JsonIgnore
    @JoinColumn
    Customer customer;

    public Account(Currency currency, Customer customer) {
        this.id = null;
        this.number = UUID.randomUUID().toString();
        this.currency = currency;
        this.balance = 0.0;
        this.customer = customer;
    }
}
