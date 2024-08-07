package org.example.clientbank.employer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.AbstractEntity;
import org.example.clientbank.customer.Customer;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "employers")
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
@Data
@NoArgsConstructor
//@NamedEntityGraph(
//        name = "graph.EmployerCustomerAccount",
//        attributeNodes = {
//                @NamedAttributeNode(value = "customers", subgraph = "subgraph.customer"),
//        },
//        subgraphs = {
//                @NamedSubgraph(
//                        name = "subgraph.customer",
//                        attributeNodes = {
//                                @NamedAttributeNode("accounts"),
//                                @NamedAttributeNode("employers")
//                        }
//                )
//        }
//)
public class Employer extends AbstractEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String address;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "customers_employers",
            joinColumns = @JoinColumn(name = "employer_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    List<Customer> customers;

    public Employer(String name, String address) {
        this.name = name;
        this.address = address;
        this.customers = new ArrayList<>();
    }
}
