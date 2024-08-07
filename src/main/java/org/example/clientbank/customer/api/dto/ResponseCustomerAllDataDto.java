package org.example.clientbank.customer.api.dto;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.account.api.dto.ResponseAccountDto;
import org.example.clientbank.dto.AbstractDto;
import org.example.clientbank.employer.api.dto.ResponseEmployerDto;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class ResponseCustomerAllDataDto extends AbstractDto {
    @JsonView(View.Admin.class)
    Long id;

    @JsonView(View.Admin.class)
    String name;

    @JsonView(View.Admin.class)
    String email;

    @JsonView(View.Admin.class)
    Integer age;

    @JsonView(View.Admin.class)
    String phone;

    @JsonView(View.Admin.class)
    LocalDateTime createdDate;

    @JsonView(View.Admin.class)
    LocalDateTime lastModifiedDate;

    @JsonView(View.Admin.class)
    List<ResponseAccountDto> accounts;

    @JsonView(View.Admin.class)
    List<ResponseEmployerDto> employers;
}
