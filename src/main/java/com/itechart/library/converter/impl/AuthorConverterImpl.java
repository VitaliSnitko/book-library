package com.itechart.library.converter.impl;

import com.itechart.library.converter.Converter;
import com.itechart.library.model.dto.AuthorDto;
import com.itechart.library.model.entity.AuthorEntity;

public class AuthorConverterImpl implements Converter<AuthorDto, AuthorEntity> {

    @Override
    public AuthorEntity toEntity(AuthorDto authorDto) {
        return AuthorEntity.builder()
                .name(authorDto.getName())
                .build();
    }

    @Override
    public AuthorDto toDto(AuthorEntity authorEntity) {
        return new AuthorDto(authorEntity.getName());
    }
}
