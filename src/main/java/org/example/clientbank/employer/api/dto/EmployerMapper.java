package org.example.clientbank.employer.api.dto;

import org.example.clientbank.employer.Employer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployerMapper {
    EmployerMapper INSTANCE = Mappers.getMapper(EmployerMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    Employer employerDtoToEmployer(RequestEmployerDto requestEmployerDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    ResponseEmployerDto employerToEmployerDto(Employer employer);
}
