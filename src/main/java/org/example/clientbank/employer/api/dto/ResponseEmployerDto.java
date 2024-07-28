package org.example.clientbank.employer.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.customer.api.dto.View;

import static lombok.AccessLevel.PRIVATE;


@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class ResponseEmployerDto {
    @JsonView(View.Admin.class)
    Long id;

    @JsonView(View.Admin.class)
    String name;

    @JsonView(View.Admin.class)
    String address;
}
