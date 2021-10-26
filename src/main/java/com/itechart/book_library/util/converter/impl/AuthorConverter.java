package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.AuthorDto;
import com.itechart.book_library.model.entity.AuthorEntity;
import com.itechart.book_library.util.converter.Converter;

public class AuthorConverter extends Converter<AuthorDto, AuthorEntity> {

    public AuthorConverter() {
        super(AuthorConverter::convertToEntity, AuthorConverter::convertToDto);
    }

    private static AuthorDto convertToDto(AuthorEntity authorEntity) {
        return new AuthorDto(authorEntity.getName());
    }

    private static AuthorEntity convertToEntity(AuthorDto authorDto) {
        return AuthorEntity.builder().name(authorDto.getName()).build();
    }
}
