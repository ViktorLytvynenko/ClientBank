package org.example.clientbank;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PUBLIC;

@MappedSuperclass
@FieldDefaults(level = PUBLIC)
@Data
public abstract class AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
