package com.itechart.library.converter.impl;

import com.itechart.library.converter.Converter;
import com.itechart.library.model.dto.GenreDto;
import com.itechart.library.model.entity.GenreEntity;

public class GenreConverterImpl implements Converter<GenreDto, GenreEntity> {

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
