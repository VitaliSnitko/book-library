package com.itechart.library.model.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@Getter
public class BookEntity extends Entity {

    private String title;
    private List<AuthorEntity> authorEntities;
    private List<GenreEntity> genreEntities;
    private String publisher;
    private Date publishDate;
    private int pageCount;
    private String ISBN;
    private String description;
    private InputStream cover;
    private int availableBookAmount;
    private int totalBookAmount;

    public void setAuthorEntities(List<AuthorEntity> authorEntities) {
        this.authorEntities = authorEntities;
    }

    public void setGenreEntities(List<GenreEntity> genreEntities) {
        this.genreEntities = genreEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookEntity that = (BookEntity) o;
        return pageCount == that.pageCount && title.equals(that.title) && publisher.equals(that.publisher)
                && publishDate.equals(that.publishDate) && ISBN.equals(that.ISBN)
                && Objects.equals(description, that.description) && Objects.equals(cover, that.cover);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, publisher, publishDate, pageCount, ISBN, description, cover);
    }
}
