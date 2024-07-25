package org.example.clientbank.customer.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.account.Account;
import org.example.clientbank.employer.Employer;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class ResponseCustomerAllDataDto {
    Long id;
    String name;
    String email;
    Integer age;
    String phone;
    List<Account> accounts;
    List<Employer> employers;
}
