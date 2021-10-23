package com.itechart.book_library.dao.api;

import com.itechart.book_library.dao.criteria.BookSpecification;
import com.itechart.book_library.model.entity.BookEntity;

import java.util.List;

public interface BookDao extends Dao<BookEntity> {
    List<BookEntity> getLimitOffset(int limit, int offset);
    List<BookEntity> getLimitOffsetBySpecification(BookSpecification specification, int limit, int offset);
    int getCount();
    int getCountBySpecification(BookSpecification specification);
    void takeBook(int id);

}
