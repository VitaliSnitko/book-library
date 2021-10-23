package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.model.entity.*;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecordDaoImpl extends BaseDao {

    private static final Logger log = Logger.getLogger(RecordDaoImpl.class);

    private static final String INSERT_QUERY = "INSERT INTO record (id, book_id, reader_id, borrow_date, due_date) VALUES (DEFAULT, ?, ?, ?, ?) RETURNING id";
    private static final String SELECT_RECORDS_BY_BOOK_ID = """
             select record.id, record.book_id, record.reader_id, record.borrow_date, record.due_date,
                    reader.email, reader.first_name
             from record
                      join reader on record.reader_id = reader.id
                      join book on record.book_id = book.id
             where book.id = ?
            """;

    public RecordEntity create(RecordEntity recordEntity) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setInt(1, recordEntity.getBookId());
            statement.setInt(2, recordEntity.getReader().getId());
            statement.setDate(3, recordEntity.getBorrowDate());
            statement.setDate(4, recordEntity.getDueDate());
            statement.execute();
            recordEntity.setId(getIdAfterInserting(statement));
        } catch (SQLException e) {
            log.error("Cannot create record ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return recordEntity;
    }

    public List<RecordEntity> getRecords(int bookId) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_RECORDS_BY_BOOK_ID)) {
            statement.setInt(1, bookId);
            return getRecordListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get books ", e);
            return null;
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    private List<RecordEntity> getRecordListFromResultSet(ResultSet resultSet) throws SQLException {
        List<RecordEntity> recordList = new ArrayList<>();
        while (resultSet.next()) {
            int readerId = resultSet.getInt(3);
            ReaderEntity reader = ReaderEntity.builder()
                    .id(readerId)
                    .email(resultSet.getString(6))
                    .name(resultSet.getString(7))
                    .build();
            RecordEntity record = RecordEntity.builder()
                    .id(resultSet.getInt(1))
                    .borrowDate(resultSet.getDate(4))
                    .dueDate(resultSet.getDate(5))
                    .bookId(resultSet.getInt(2))
                    .reader(reader)
                    .build();
            recordList.add(record);
        }
        return recordList;
    }

}
