package org.example.clientbank.employer.api.dto;

import org.example.clientbank.customer.api.dto.CustomerMapper;
import org.example.clientbank.employer.Employer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomerMapper.class})
public interface EmployerMapper {
    EmployerMapper INSTANCE = Mappers.getMapper(EmployerMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    Employer employerDtoToEmployer(RequestEmployerDto requestEmployerDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
//    @Mapping(source = "customers", target = "customers")
    ResponseEmployerDto employerToEmployerDto(Employer employer);
}
