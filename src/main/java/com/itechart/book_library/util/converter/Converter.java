package com.itechart.book_library.util.converter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Converter<T, U> {

    private final Function<T, U> fromDto;
    private final Function<U, T> fromEntity;

    public Converter(final Function<T, U> fromDto, final Function<U, T> fromEntity) {
        this.fromDto = fromDto;
        this.fromEntity = fromEntity;
    }

    public final U toEntity(final T dto) {
        return fromDto.apply(dto);
    }

    public final T toDto(final U entity) {
        return fromEntity.apply(entity);
    }

    public final List<U> toEntities(final Collection<T> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public final List<T> toDtos(final Collection<U> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
}
