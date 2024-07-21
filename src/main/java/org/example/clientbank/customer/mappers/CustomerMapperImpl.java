package org.example.clientbank.customer.mappers;

import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerDto dto) {
        if (dto == null) {
            return null;
        }
        return new Customer(dto.getName(), dto.getEmail(), dto.getAge());
    }

    @Override
    public CustomerDto toDto(Customer entity) {
        if (entity == null) {
            return null;
        }
        return new CustomerDto(entity.getName(), entity.getEmail(), entity.getAge());
    }

    @Override
    public void updateEntityFromDto(Customer entity, CustomerDto dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getAge() != null) {
            entity.setAge(dto.getAge());
        }
    }
}
