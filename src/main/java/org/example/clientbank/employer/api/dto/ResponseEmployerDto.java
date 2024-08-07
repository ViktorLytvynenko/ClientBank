package org.example.clientbank.employer.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.customer.api.dto.View;
import org.example.clientbank.dto.AbstractDto;

import static lombok.AccessLevel.PRIVATE;


@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class ResponseEmployerDto extends AbstractDto {
    @JsonView(View.Admin.class)
    Long id;

    @JsonView(View.Admin.class)
    String name;

    @JsonView(View.Admin.class)
    String address;

//    @JsonView(View.Admin.class)
//    List<ResponseCustomerDto> customers;
}
