package org.example.clientbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.customer.api.dto.View;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponseDto<T extends AbstractDto> {
    @JsonView(View.Admin.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T dto;

    @JsonView(View.Admin.class)
    String message;
}
