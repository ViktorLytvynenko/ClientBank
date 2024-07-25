package org.example.clientbank.employer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;


@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class ResponseEmployerDto {
    Long id;
    String name;
    String address;
}
