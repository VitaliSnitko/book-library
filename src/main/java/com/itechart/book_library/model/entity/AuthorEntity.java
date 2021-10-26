package com.itechart.book_library.model.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@SuperBuilder
@Getter
public class AuthorEntity extends Entity {

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuthorEntity that = (AuthorEntity) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
