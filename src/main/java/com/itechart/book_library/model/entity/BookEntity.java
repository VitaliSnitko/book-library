package com.itechart.book_library.model.entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@NoArgsConstructor
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InputStream getCover() {
        return cover;
    }

    public void setCover(InputStream cover) {
        this.cover = cover;
    }

    public List<AuthorEntity> getAuthorEntities() {
        return authorEntities;
    }

    public void setAuthorEntities(List<AuthorEntity> authorEntities) {
        this.authorEntities = authorEntities;
    }

    public List<GenreEntity> getGenreEntities() {
        return genreEntities;
    }

    public void setGenreEntities(List<GenreEntity> genreEntities) {
        this.genreEntities = genreEntities;
    }

    public int getAvailableBookAmount() {
        return availableBookAmount;
    }

    public void setAvailableBookAmount(int availableBookAmount) {
        this.availableBookAmount = availableBookAmount;
    }

    public int getTotalBookAmount() {
        return totalBookAmount;
    }

    public void setTotalBookAmount(int totalBookAmount) {
        this.totalBookAmount = totalBookAmount;
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
