package org.example.clientbank.account.api.dto;

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
public class ResponseAccountDto extends AbstractDto {

    @JsonView(View.Admin.class)
    Long id;

    @JsonView(View.Admin.class)
    String number;

    @JsonView(View.Admin.class)
    String currency;

    @JsonView(View.Admin.class)
    Double balance;
}
