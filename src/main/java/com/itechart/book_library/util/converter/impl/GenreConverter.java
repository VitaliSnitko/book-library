package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.GenreDto;
import com.itechart.book_library.model.entity.GenreEntity;
import com.itechart.book_library.util.converter.Converter;

public class GenreConverter extends Converter<GenreDto, GenreEntity> {
    public GenreConverter() {
        super(GenreConverter::convertToEntity, GenreConverter::convertToDto);
    }

    private static GenreDto convertToDto(GenreEntity genreEntity) {
        return new GenreDto(genreEntity.getName());
    }

    private static GenreEntity convertToEntity(GenreDto genreDto) {
        return new GenreEntity(genreDto.getName());
    }
}
