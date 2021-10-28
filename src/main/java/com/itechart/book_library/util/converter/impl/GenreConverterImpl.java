package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.GenreDto;
import com.itechart.book_library.model.entity.GenreEntity;
import com.itechart.book_library.util.converter.api.GenreConverter;

public class GenreConverterImpl implements GenreConverter {

    @Override
    public GenreEntity toEntity(GenreDto genreDto) {
        return GenreEntity.builder()
                .name(genreDto.getName())
                .build();
    }

    @Override
    public GenreDto toDto(GenreEntity genreEntity) {
        return new GenreDto(genreEntity.getName());
    }
}
