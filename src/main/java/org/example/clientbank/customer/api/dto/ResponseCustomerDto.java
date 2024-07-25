package org.example.clientbank.customer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;


@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class ResponseCustomerDto {
    Long id;
    String name;
    String email;
    Integer age;
    String phone;
}
