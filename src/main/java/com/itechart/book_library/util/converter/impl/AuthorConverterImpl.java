package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.AuthorDto;
import com.itechart.book_library.model.entity.AuthorEntity;
import com.itechart.book_library.util.converter.api.AuthorConverter;

public class AuthorConverterImpl implements AuthorConverter {

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
