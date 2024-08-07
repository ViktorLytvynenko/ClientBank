package org.example.clientbank.customer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.AbstractEntity;
import org.example.clientbank.account.Account;
import org.example.clientbank.employer.Employer;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "customers")
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
//@NamedEntityGraph(
//        name = "graph.CustomerAccountEmployer",
//        attributeNodes = {
//                @NamedAttributeNode("employers"),
//                @NamedAttributeNode("accounts")
//        }
//)
public class Customer extends AbstractEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    Integer age;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String phone;

    @OneToMany(mappedBy = "customer",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<Account> accounts;

    @ManyToMany(mappedBy = "customers", cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST}, fetch = FetchType.LAZY)
    List<Employer> employers;

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.accounts = new ArrayList<>();
        this.employers = new ArrayList<>();
    }

    public Customer(String name, String email, Integer age, String password, String phone) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
        this.phone = phone;
        this.accounts = new ArrayList<>();
        this.employers = new ArrayList<>();
    }
}
