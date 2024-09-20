package com.example.Artisan.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * The DtoEntityConverter class is responsible for converting between DTOs (Data Transfer Objects) and entities.
 * It uses the ModelMapper library for mapping the properties between the source and target objects.
 */
@Component
public class DtoEntityConverter {

    private final ModelMapper modelMapper;

    public DtoEntityConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <D, E> D convertToDto(E entity, Class<D> dtoClass) {
        if (entity == null) {
            return null; // Return null if the source entity is null
        }
        return modelMapper.map(entity, dtoClass);
    }

    public <D, E> E convertToEntity(D dto, Class<E> entityClass) {
        if (dto == null) {
            return null; // Return null if the source dto is null
        }
        return modelMapper.map(dto, entityClass);
    }
}

