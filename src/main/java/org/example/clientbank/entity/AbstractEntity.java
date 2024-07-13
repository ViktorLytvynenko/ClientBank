package org.example.clientbank.entity;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PUBLIC;


@FieldDefaults(level = PUBLIC)
@Data
public abstract class AbstractEntity {
    Long id;
}
