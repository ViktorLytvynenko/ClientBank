package org.example.clientbank.employer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.AbstractEntity;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "employers")
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
@Data
@NoArgsConstructor
public class Employer extends AbstractEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String address;
}
