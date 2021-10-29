package com.itechart.library.dao;

import com.itechart.library.dao.criteria.BookSpecification;
import com.itechart.library.model.entity.BookEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BookDao extends Dao<BookEntity> {

    List<BookEntity> getLimitOffsetBySpecification(BookSpecification specification, int limit, int offset);

    int getCountBySpecification(BookSpecification specification);

    int getAvailableBookAmount(int id);

    int getTotalBookAmount(int id);

    void updateAvailableAmount(int amount, int id, Connection connection) throws SQLException;

    void updateTotalAmount(int amount, int id, Connection connection) throws SQLException;
}
