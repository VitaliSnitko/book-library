package com.itechart.book_library.service;

import com.itechart.book_library.connection.ConnectionPool;
import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.dao.api.ReaderDao;
import com.itechart.book_library.dao.api.RecordDao;
import com.itechart.book_library.dao.impl.BookDaoImpl;
import com.itechart.book_library.dao.impl.ReaderDaoImpl;
import com.itechart.book_library.dao.impl.RecordDaoImpl;
import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.ReaderEntity;
import com.itechart.book_library.model.entity.RecordEntity;
import com.itechart.book_library.model.entity.Status;
import com.itechart.book_library.util.converter.api.ReaderConverter;
import com.itechart.book_library.util.converter.api.RecordConverter;
import com.itechart.book_library.util.converter.impl.ReaderConverterImpl;
import com.itechart.book_library.util.converter.impl.RecordConverterImpl;
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
    private final RecordDao recordDao = BaseDao.getDao(RecordDaoImpl.class);
    private final BookDao bookDao = BaseDao.getDao(BookDaoImpl.class);

    private final ReaderConverter readerConverter = new ReaderConverterImpl();
    private final RecordConverter recordConverter = new RecordConverterImpl();

    public void addRecords(String[] emails, String[] names, String[] periods, int bookId) {
        List<ReaderEntity> readerEntities = getReaderEntities(emails, names);
        List<RecordEntity> recordEntities = getRecordEntities(readerEntities, periods, bookId);
        createReaderRecords(readerEntities, recordEntities);
    }

    public void updateRecords(List<RecordDto> recordDtos) {
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);
        try {
            for (RecordDto recordDto : recordDtos) {
                updateRecord(recordDto, connection);
            }
            commit(connection);
        } catch (SQLException e) {
            rollback(connection);
            log.error(e);
        }
        setAutoCommit(connection, true);
        connectionPool.returnToPool(connection);
    }

    private void updateRecord(RecordDto recordDto, Connection connection) throws SQLException {
        if (recordDto.getStatus() == Status.RETURNED) {
            bookDao.returnBook(recordDto.getBookId(), connection);
        } else {
            bookDao.loseBook(recordDto.getBookId(), connection);
        }
        recordDao.update(recordConverter.toEntity(recordDto), connection);
    }

    public List<RecordDto> getRecords(int bookId, boolean areRecordsActive) {
        return recordConverter.toDtos(recordDao.getRecordsByBookId(bookId, areRecordsActive));
    }

    public List<ReaderDto> getAllReaders() {
        return readerConverter.toDtos(readerDao.getAll());
    }

    private List<ReaderEntity> getReaderEntities(String[] emails, String[] names) {
        List<ReaderEntity> readerEntities = new ArrayList<>();
        for (int i = 0; i < emails.length; i++) {
            readerEntities.add(ReaderEntity.builder().email(emails[i]).name(names[i]).build());
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
                saveReaderRecord(readerEntities.get(i), recordEntities.get(i), connection);
                commit(connection);
            } catch (SQLException e) {
                rollback(connection);
                log.error(e);
            }
        }
        setAutoCommit(connection, true);
        connectionPool.returnToPool(connection);
    }

    private void saveReaderRecord(ReaderEntity readerEntity, RecordEntity recordEntity, Connection connection) throws SQLException {
        Optional<ReaderEntity> readerByEmailOptional = readerDao.getByEmail(readerEntity.getEmail(), connection);
        ReaderEntity newReaderEntity;
        if (readerByEmailOptional.isEmpty()) {
            newReaderEntity = readerDao.create(readerEntity, connection);
        } else {
            readerDao.update(readerEntity, connection);
            newReaderEntity = readerByEmailOptional.get();
        }
        recordEntity.getReader().setId(newReaderEntity.getId());

        if (readerDao.getByEmailInCurrentBook(newReaderEntity.getEmail(), recordEntity.getBookId(), connection).isEmpty()) {
            recordDao.create(recordEntity, connection);
            try {
                bookDao.takeBook(recordEntity.getBookId(), connection);
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
            log.error(e);
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

