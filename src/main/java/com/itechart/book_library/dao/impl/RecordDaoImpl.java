package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.RecordDao;
import com.itechart.book_library.model.entity.ReaderEntity;
import com.itechart.book_library.model.entity.RecordEntity;
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
                    reader.email, reader.first_name, record.return_date
             from record
                      join reader on record.reader_id = reader.id
                      join book on record.book_id = book.id
             where book.id = ? and case when ? then record.status = 'BORROWED' else record.status != 'BORROWED' end;
            """;
    private static final String UPDATE_STATUS_QUERY = "update record set status = ?, return_date = current_date where id = ?";
    @Override
    public RecordEntity create(RecordEntity recordEntity, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setInt(1, recordEntity.getBookId());
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
    public List<RecordEntity> getRecordsByBookId(int bookId, boolean areRecordsActive) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_RECORDS_BY_BOOK_ID_QUERY)) {
            statement.setInt(1, bookId);
            statement.setBoolean(2, areRecordsActive);
            return getRecordListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get books ", e);
            return new ArrayList<>();
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
                    .returnDate(resultSet.getDate(8))
                    .bookId(resultSet.getInt(2))
                    .reader(reader)
                    .build();
            recordList.add(record);
        }
        return recordList;
    }

}
