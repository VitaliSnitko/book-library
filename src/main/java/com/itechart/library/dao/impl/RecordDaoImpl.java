package com.itechart.library.dao.impl;

import com.itechart.library.dao.BaseDao;
import com.itechart.library.dao.RecordDao;
import com.itechart.library.model.entity.BookEntity;
import com.itechart.library.model.entity.ReaderEntity;
import com.itechart.library.model.entity.RecordEntity;
import com.itechart.library.model.entity.Status;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnusedAssignment")
@Log4j
public class RecordDaoImpl extends BaseDao implements RecordDao {

    private static final String INSERT_QUERY = "INSERT INTO record (id, book_id, reader_id, borrow_date, due_date) VALUES (DEFAULT, ?, ?, ?, ?) RETURNING id";
    private static final String SELECT_RECORDS_BY_BOOK_ID_QUERY = """
            select reader.email, reader.first_name, record.*
            from record
                     join reader on record.reader_id = reader.id
                     join book on record.book_id = book.id
            where book.id = ?;""";
    private static final String UPDATE_STATUS_QUERY = "update record set status = ?, return_date = current_date where id = ?";
    private static final String SELECT_BY_EMAIL_AND_BOOK_QUERY = """
            SELECT reader.*, record.status FROM reader
            JOIN record ON reader.id = record.reader_id
            WHERE email = ? AND record.book_id = ?;""";
    private static final String SELECT_NEAREST_AVAILABLE_DATE_QUERY = "select min(record.due_date) from record where book_id = ?";
    public static final String ID_LABEL = "id";
    public static final String EMAIL_LABEL = "email";
    public static final String FIRST_NAME_LABEL = "first_name";
    public static final String BOOK_ID_LABEL = "book_id";
    public static final String STATUS_LABEL = "status";
    public static final String READER_ID_LABEL = "reader_id";
    public static final String BORROW_DATE_LABEL = "borrow_date";
    public static final String DUE_DATE_LABEL = "due_date";
    public static final String RETURN_DATE_LABEL = "return_date";

    @Override
    public RecordEntity create(RecordEntity recordEntity, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            int i = 1;
            statement.setInt(i++, recordEntity.getBook().getId());
            statement.setInt(i++, recordEntity.getReader().getId());
            statement.setDate(i++, recordEntity.getBorrowDate());
            statement.setDate(i++, recordEntity.getDueDate());
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
    public RecordEntity update(RecordEntity record, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS_QUERY)) {
            int i = 1;
            statement.setString(i++, record.getStatus().name());
            statement.setInt(i++, record.getId());
            statement.executeUpdate();
        }
        return record;
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
            int i = 1;
            statement.setString(i++, email);
            statement.setInt(i++, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ReaderEntity reader = ReaderEntity.builder()
                        .id(resultSet.getInt(ID_LABEL))
                        .email(resultSet.getString(EMAIL_LABEL))
                        .name(resultSet.getString(FIRST_NAME_LABEL))
                        .build();
                recordEntity = Optional.of(RecordEntity.builder()
                        .status(Status.valueOf(resultSet.getString(STATUS_LABEL)))
                        .reader(reader)
                        .build());
            }
        } catch (SQLException e) {
            log.error("Cannot get by email ", e);
        }
        return recordEntity;
    }

    @Override
    public Date getNearestAvailableDateByBookId(int bookId) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_NEAREST_AVAILABLE_DATE_QUERY)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDate(1);
            }
        } catch (SQLException e) {
            log.error("Cannot get books ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return null;
    }

    private List<RecordEntity> getRecordListFromResultSet(ResultSet resultSet) throws SQLException {
        List<RecordEntity> recordList = new ArrayList<>();
        while (resultSet.next()) {
            int readerId = resultSet.getInt(READER_ID_LABEL);
            BookEntity book = BookEntity.builder()
                    .id(resultSet.getInt(BOOK_ID_LABEL))
                    .build();
            ReaderEntity reader = ReaderEntity.builder()
                    .id(readerId)
                    .email(resultSet.getString(EMAIL_LABEL))
                    .name(resultSet.getString(FIRST_NAME_LABEL))
                    .build();
            RecordEntity record = RecordEntity.builder()
                    .id(resultSet.getInt(ID_LABEL))
                    .borrowDate(resultSet.getDate(BORROW_DATE_LABEL))
                    .dueDate(resultSet.getDate(DUE_DATE_LABEL))
                    .returnDate(resultSet.getDate(RETURN_DATE_LABEL))
                    .status(Status.valueOf(resultSet.getString(STATUS_LABEL)))
                    .book(book)
                    .reader(reader)
                    .build();
            recordList.add(record);
        }
        return recordList;
    }

}
