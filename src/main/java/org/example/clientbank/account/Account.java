package org.example.clientbank.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.AbstractEntity;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.Customer;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "accounts")
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
@Data
@NoArgsConstructor
//@NamedEntityGraph(
//        name = "graph.AccountCustomerEmployer",
//        attributeNodes = {
//                @NamedAttributeNode(value = "customer", subgraph = "subgraph.customer")
//        },
//        subgraphs = {
//                @NamedSubgraph(
//                        name = "subgraph.customer",
//                        attributeNodes = {
//                                @NamedAttributeNode("employers"),
//                                @NamedAttributeNode("accounts")
//                        }
//                )
//        }
//)
public class Account extends AbstractEntity {

    @Column(nullable = false)
    String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Currency currency;

    @Column(nullable = false)
    Double balance;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
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
