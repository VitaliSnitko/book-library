package com.itechart.book_library.service;

import com.itechart.book_library.connection.ConnectionPool;
import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.dao.api.ReaderDao;
import com.itechart.book_library.dao.impl.BookDaoImpl;
import com.itechart.book_library.dao.impl.ReaderDaoImpl;
import com.itechart.book_library.dao.impl.RecordDaoImpl;
import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.ReaderEntity;
import com.itechart.book_library.model.entity.RecordEntity;
import com.itechart.book_library.util.converter.Converter;
import com.itechart.book_library.util.converter.impl.ReaderConverter;
import com.itechart.book_library.util.converter.impl.RecordConverter;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
public enum ReaderService {
    INSTANCE;

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private final ReaderDao readerDao = BaseDao.getDao(ReaderDaoImpl.class);
    private final RecordDaoImpl recordDao = BaseDao.getDao(RecordDaoImpl.class);
    private final BookDao bookDao = BaseDao.getDao(BookDaoImpl.class);

    private final Converter<ReaderDto, ReaderEntity> readerConverter = new ReaderConverter();
    private final Converter<RecordDto, RecordEntity> recordConverter = new RecordConverter();

    public void addRecords(String[] emails, String[] names, String[] periods, int bookId) {
        List<ReaderEntity> readerEntities = getReaderEntities(emails, names);
        List<RecordEntity> recordEntities = getRecordEntities(readerEntities, periods, bookId);

        createReaderRecords(readerEntities, recordEntities);
    }

    public List<RecordDto> getRecords(int bookId) {
        return recordConverter.toDtos(recordDao.getRecords(bookId));
    }

    public List<ReaderDto> getAllReaders() {
        return readerConverter.toDtos(readerDao.getAll());
    }

    private List<ReaderEntity> getReaderEntities(String[] emails, String[] names) {
        List<ReaderEntity> readerEntities = new ArrayList<>();
        for (int i = 0; i < emails.length; i++) {
            readerEntities.add(new ReaderEntity(emails[i], names[i]));
        }
        return readerEntities;
    }

    private List<RecordEntity> getRecordEntities(List<ReaderEntity> readerEntities, String[] periods, int bookId) {
        List<RecordEntity> recordEntities = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < periods.length; i++) {
            recordEntities.add(RecordEntity.builder()
                    .borrowDate(Date.valueOf(now))
                    .dueDate(Date.valueOf(now.plusMonths(Integer.parseInt(periods[i]))))
                    .bookId(bookId)
                    .reader(readerEntities.get(i))
                    .build());
        }
        return recordEntities;
    }

    private void createReaderRecords(List<ReaderEntity> readerEntities, List<RecordEntity> recordEntities) {
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);
        for (int i = 0; i < readerEntities.size(); i++) {
            try {
                manageReaderRecord(readerEntities.get(i), recordEntities.get(i), connection);
                commit(connection);
            } catch (SQLException e) {
                rollback(connection);
                log.error(e);
            }
        }
        setAutoCommit(connection, true);
        connectionPool.returnToPool(connection);
    }

    private void manageReaderRecord(ReaderEntity readerEntity, RecordEntity recordEntity, Connection connection) throws SQLException {
        Optional<ReaderEntity> readerByEmailOptional = readerDao.getByEmail(readerEntity.getEmail(), connection);

        if (readerByEmailOptional.isEmpty()) {
            readerEntity = readerDao.create(readerEntity, connection);
        } else {
            readerEntity = readerByEmailOptional.get();
        }
        recordEntity.getReader().setId(readerEntity.getId());

        if (readerDao.getByEmailInCurrentBook(readerEntity.getEmail(), recordEntity.getBookId(), connection).isEmpty()) {
            recordDao.create(recordEntity, connection);
            try {
                bookDao.takeBook(recordEntity.getBookId(), connection);
                commit(connection);
            } catch (SQLException e) {
                log.warn("No books available");
                rollback(connection);
            }
        }
    }

    private void setAutoCommit(Connection connection, boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            log.error("Cannot set autoCommit", e);
        }
    }

    private void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            log.error(e);
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error(e);
        }
    }
}

