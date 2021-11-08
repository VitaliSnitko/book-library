package com.itechart.library.dao;

import com.itechart.library.dao.criteria.BookSpecification;
import com.itechart.library.model.entity.BookEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BookDao extends Dao<BookEntity> {

    /**
     * @param specification contains search parameters (optionally in regex form)
     * @param limit specifies book amount on one page
     * @param offset specifies number of books to skip
     * @return Book list that meets specification and locates in limit-offset range
     */
    List<BookEntity> getLimitOffsetBySpecification(BookSpecification specification, int limit, int offset);

    /**
     * @param specification contains search parameters (optionally in regex form)
     * @return Amount of books that meets specification
     */
    int getCount(BookSpecification specification);

    /**
     * @param id book id
     * @return Amount of books that have at least one available book
     */
    int getAvailableBookCount(int id);

    /**
     * @param id book id
     * @return Amount of books in library
     */
    int getTotalBookAmount(int id);

    void updateAvailableAmount(int amount, int id, Connection connection) throws SQLException;

    void updateTotalAmount(int amount, int id, Connection connection) throws SQLException;
}
