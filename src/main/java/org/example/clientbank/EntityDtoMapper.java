package org.example.clientbank;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityDtoMapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);
}