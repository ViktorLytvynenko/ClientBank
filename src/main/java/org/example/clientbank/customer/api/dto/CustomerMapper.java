package org.example.clientbank.customer.api.dto;

import org.example.clientbank.account.api.dto.AccountMapper;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.employer.api.dto.EmployerMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AccountMapper.class, EmployerMapper.class})
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "password", target = "password")
    Customer customerDtoToCustomer(RequestCustomerDto requestCustomerDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    ResponseCustomerDto customerToCustomerDto(Customer customer);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    @Mapping(source = "accounts", target = "accounts")
    @Mapping(source = "employers", target = "employers")
    ResponseCustomerAllDataDto customerToCustomerAllDataDto(Customer customer);
}
