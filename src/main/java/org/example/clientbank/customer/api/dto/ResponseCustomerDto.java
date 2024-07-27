package org.example.clientbank.customer.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;


@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class ResponseCustomerDto {
    @JsonView(View.Base.class)
    Long id;

    @JsonView(View.Base.class)
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
}
