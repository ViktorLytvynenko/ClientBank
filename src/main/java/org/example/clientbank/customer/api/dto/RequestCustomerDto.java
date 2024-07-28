package org.example.clientbank.customer.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.customer.api.dto.validation.ValidPassword;


import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestCustomerDto {

    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    String email;

    @NotNull(message = "Age must not be blank")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 190, message = "Age must not exceed 190")
    Integer age;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    String phone;

    @NotBlank(message = "Password must not be blank")
    @ValidPassword
    String password;
}
