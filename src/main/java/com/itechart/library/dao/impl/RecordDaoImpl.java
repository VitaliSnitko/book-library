package com.itechart.library.dao.impl;

import com.itechart.library.dao.BaseDao;
import com.itechart.library.dao.RecordDao;
import com.itechart.library.model.entity.BookEntity;
import com.itechart.library.model.entity.ReaderEntity;
import com.itechart.library.model.entity.RecordEntity;
import com.itechart.library.model.entity.Status;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
public class RecordDaoImpl extends BaseDao implements RecordDao {

    private static final String INSERT_QUERY = "INSERT INTO record (id, book_id, reader_id, borrow_date, due_date) VALUES (DEFAULT, ?, ?, ?, ?) RETURNING id";
    private static final String SELECT_RECORDS_BY_BOOK_ID_QUERY = """
             select record.id, record.book_id, record.reader_id, record.borrow_date, record.due_date,
                    reader.email, reader.first_name, record.return_date, record.status
             from record
                      join reader on record.reader_id = reader.id
                      join book on record.book_id = book.id
             where book.id = ?;
            """;
    private static final String UPDATE_STATUS_QUERY = "update record set status = ?, return_date = current_date where id = ?";
    private static final String SELECT_BY_EMAIL_AND_BOOK_QUERY = """
            SELECT reader.*, record.status FROM reader
            JOIN record ON reader.id = record.reader_id
            WHERE email = ? AND record.book_id = ?;""";

    @Override
    public RecordEntity create(RecordEntity recordEntity, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setInt(1, recordEntity.getBook().getId());
            statement.setInt(2, recordEntity.getReader().getId());
            statement.setDate(3, recordEntity.getBorrowDate());
            statement.setDate(4, recordEntity.getDueDate());
            statement.execute();
            recordEntity.setId(getIdAfterInserting(statement));
        }
        return recordEntity;
    }

    @Override
    public Optional<RecordEntity> getById(int id) {
        return Optional.empty();
    }

    @Override
    public void update(RecordEntity record, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS_QUERY)) {
            statement.setString(1, record.getStatus().name());
            statement.setInt(2, record.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer[] ids) {
    }

    @Override
    public void delete(Integer[] ids, Connection connection) throws SQLException {
    }

    @Override
    public List<RecordEntity> getRecordsByBookId(int bookId) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_RECORDS_BY_BOOK_ID_QUERY)) {
            statement.setInt(1, bookId);
            return getRecordListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get books ", e);
            return new ArrayList<>();
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    @Override
    public Optional<RecordEntity> getByEmailInCurrentBook(String email, int bookId, Connection connection) {
        Optional<RecordEntity> recordEntity = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL_AND_BOOK_QUERY)) {
            statement.setString(1, email);
            statement.setInt(2, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ReaderEntity reader = ReaderEntity.builder()
                        .id(resultSet.getInt(1))
                        .email(resultSet.getString(2))
                        .name(resultSet.getString(3))
                        .build();
                recordEntity = Optional.of(RecordEntity.builder()
                        .status(Status.valueOf(resultSet.getString(4)))
                        .reader(reader)
                        .build());
            }
        } catch (SQLException e) {
            log.error("Cannot get by email ", e);
        }
        return recordEntity;
    }

    private List<RecordEntity> getRecordListFromResultSet(ResultSet resultSet) throws SQLException {
        List<RecordEntity> recordList = new ArrayList<>();
        while (resultSet.next()) {
            int readerId = resultSet.getInt(3);
            BookEntity book = BookEntity.builder()
                    .id(resultSet.getInt(2))
                    .build();
            ReaderEntity reader = ReaderEntity.builder()
                    .id(readerId)
                    .email(resultSet.getString(6))
                    .name(resultSet.getString(7))
                    .build();
            RecordEntity record = RecordEntity.builder()
                    .id(resultSet.getInt(1))
                    .borrowDate(resultSet.getDate(4))
                    .dueDate(resultSet.getDate(5))
                    .returnDate(resultSet.getDate(8))
                    .status(Status.valueOf(resultSet.getString(9)))
                    .book(book)
                    .reader(reader)
                    .build();
            recordList.add(record);
        }
        return recordList;
    }

}
