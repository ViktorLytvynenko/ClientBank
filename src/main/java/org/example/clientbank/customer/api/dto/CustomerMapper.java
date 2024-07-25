package org.example.clientbank.customer.api.dto;

import org.example.clientbank.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "age", target = "age")
    Customer customerDtoToCustomer(RequestCustomerDto requestCustomerDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "age", target = "age")
    ResponseCustomerDto customerToCustomerDto(Customer customer);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "accounts", target = "accounts")
    @Mapping(source = "employers", target = "employers")
    ResponseCustomerAllDataDto customerToCustomerAllDataDto(Customer customer);


}
