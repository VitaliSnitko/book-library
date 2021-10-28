package com.itechart.book_library.util.converter.api;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Converter<Dto, Entity> {

    Entity toEntity(Dto dto);

    Dto toDto(Entity entity);

    default List<Dto> toDtos(Collection<Entity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    default List<Entity> toEntities(Collection<Dto> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
