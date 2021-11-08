package com.itechart.library.service;

import com.itechart.library.dao.criteria.BookSpecification;
import com.itechart.library.model.dto.BookDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BookService {

    void create(BookDto bookDto, Connection connection) throws SQLException;

    void update(BookDto bookDto, Connection connection) throws SQLException;

    List<BookDto> getLimitOffsetBySpecification(BookSpecification specification, int bookAmountOnOnePage, int page);

    BookDto getById(int id);

    int getBookCount(BookSpecification specification);

    void delete(String[] ids);
}
