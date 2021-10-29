package com.itechart.library.service;

import com.itechart.library.connection.ConnectionPool;
import com.itechart.library.converter.BookConverter;
import com.itechart.library.converter.ReaderConverter;
import com.itechart.library.converter.RecordConverter;
import com.itechart.library.converter.impl.BookConverterImpl;
import com.itechart.library.converter.impl.ReaderConverterImpl;
import com.itechart.library.converter.impl.RecordConverterImpl;
import com.itechart.library.dao.BaseDao;
import com.itechart.library.dao.BookDao;
import com.itechart.library.dao.ReaderDao;
import com.itechart.library.dao.RecordDao;
import com.itechart.library.dao.impl.BookDaoImpl;
import com.itechart.library.dao.impl.ReaderDaoImpl;
import com.itechart.library.dao.impl.RecordDaoImpl;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.dto.ReaderDto;
import com.itechart.library.model.dto.RecordDto;
import com.itechart.library.model.entity.ReaderEntity;
import com.itechart.library.model.entity.RecordEntity;
import com.itechart.library.model.entity.Status;
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

    private final BookConverter bookConverter = new BookConverterImpl();
    private final ReaderConverter readerConverter = new ReaderConverterImpl();
    private final RecordConverter recordConverter = new RecordConverterImpl();

    private int availableBookAmount;
    private int totalBookAmount;
    private int bookId;

    public void saveRecords(String[] emails, String[] names, String[] periods, BookDto bookDto) {
        List<ReaderEntity> readerEntities = getReaderEntities(emails, names);
        List<RecordEntity> recordEntities = getRecordEntities(readerEntities, periods, bookDto);
        saveReaderRecords(readerEntities, recordEntities);
    }

    public void updateRecords(List<RecordDto> recordDtos) {
        if (recordDtos.size() == 0) {
            return;
        }
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);
        try {
            this.bookId = recordDtos.get(0).getBook().getId();
            this.availableBookAmount = bookDao.getAvailableBookAmount(bookId);
            this.totalBookAmount = bookDao.getTotalBookAmount(bookId);
            for (RecordDto recordDto : recordDtos) {
                updateRecord(connection, recordDto);
            }
            commit(connection);
        } catch (SQLException e) {
            rollback(connection);
            log.error(e);
        }
        setAutoCommit(connection, true);
        connectionPool.returnToPool(connection);
    }

    private void updateRecord(Connection connection, RecordDto recordDto) throws SQLException {
        if (recordDto.getStatus() == Status.RETURNED) {
            bookDao.updateAvailableAmount(++availableBookAmount, bookId, connection);
        } else {
            bookDao.updateTotalAmount(--totalBookAmount, bookId, connection);
        }
        recordDao.update(recordConverter.toEntity(recordDto), connection);
    }

    public List<RecordDto> getRecords(int bookId) {
        return recordConverter.toDtos(recordDao.getRecordsByBookId(bookId));
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

    private List<RecordEntity> getRecordEntities(List<ReaderEntity> readerEntities, String[] periods, BookDto bookDto) {
        List<RecordEntity> recordEntities = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < periods.length; i++) {
            recordEntities.add(RecordEntity.builder()
                    .borrowDate(Date.valueOf(now))
                    .dueDate(Date.valueOf(now.plusMonths(Integer.parseInt(periods[i]))))
                    .book(bookConverter.toEntity(bookDto))
                    .reader(readerEntities.get(i))
                    .build());
        }
        return recordEntities;
    }

    private void saveReaderRecords(List<ReaderEntity> readerEntities, List<RecordEntity> recordEntities) {
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);
        this.bookId = recordEntities.get(0).getBook().getId();
        this.availableBookAmount = bookDao.getAvailableBookAmount(bookId);
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

        Optional<RecordEntity> recordOptional = recordDao.getByEmailInCurrentBook(newReaderEntity.getEmail(), recordEntity.getBook().getId(), connection);
        if (recordOptional.isEmpty() || recordOptional.get().getStatus() != Status.BORROWED) {
            recordDao.create(recordEntity, connection);
            try {
                bookDao.updateAvailableAmount(--availableBookAmount, this.bookId, connection);
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

