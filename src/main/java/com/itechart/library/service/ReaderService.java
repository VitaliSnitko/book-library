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
import com.itechart.library.scheduler.ScheduleManager;
import com.itechart.library.scheduler.job.DueDateReminderJob;
import com.itechart.library.scheduler.job.OverdueReminderJob;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Log4j
public enum ReaderService {
    INSTANCE;

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private final ScheduleManager scheduleManager = ScheduleManager.getInstance();

    private final ReaderDao readerDao = BaseDao.getDao(ReaderDaoImpl.class);
    private final RecordDao recordDao = BaseDao.getDao(RecordDaoImpl.class);
    private final BookDao bookDao = BaseDao.getDao(BookDaoImpl.class);

    private final BookConverter bookConverter = new BookConverterImpl();
    private final ReaderConverter readerConverter = new ReaderConverterImpl();
    private final RecordConverter recordConverter = new RecordConverterImpl();

    private static final String DUE_DATE_REMINDER_STRING = "DueDateReminder";
    private static final String OVERDUE_REMINDER_STRING = "OverdueReminder";

    public void createReaderRecords(String[] emails, String[] names, String[] periods, BookDto bookDto) {
        List<ReaderEntity> readerEntities = getReaderEntities(emails, names);
        List<RecordEntity> recordEntities = getRecordEntities(readerEntities, periods, bookDto);
        createReaderRecords(readerEntities, recordEntities);
    }

    public void updateRecords(List<RecordDto> recordDtos) {
        if (recordDtos.size() == 0) {
            return;
        }
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);
        try {
            int bookId = recordDtos.get(0).getBook().getId();
            int availableBookAmount = bookDao.getAvailableBookAmount(bookId);
            int totalBookAmount = bookDao.getTotalBookAmount(bookId);
            for (RecordDto recordDto : recordDtos) {
                if (recordDto.getStatus() == Status.RETURNED) {
                    availableBookAmount++;
                } else {
                    totalBookAmount--;
                }
                bookDao.updateAvailableAmount(availableBookAmount, bookId, connection);
                bookDao.updateTotalAmount(totalBookAmount, bookId, connection);
                updateRecord(connection, recordDto);
                unscheduleJobs(recordDto);
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
        recordDao.update(recordConverter.toEntity(recordDto), connection);
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

    private void createReaderRecords(List<ReaderEntity> readerEntities, List<RecordEntity> recordEntities) {
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);
        int bookId = recordEntities.get(0).getBook().getId();
        int availableBookAmount = bookDao.getAvailableBookAmount(bookId);
        try {
            for (int i = 0; i < readerEntities.size(); i++) {
                ReaderEntity readerEntity = readerEntities.get(i);
                RecordEntity recordEntity = recordEntities.get(i);
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
                        bookDao.updateAvailableAmount(--availableBookAmount, bookId, connection);
                        //scheduleJobs(recordEntities.get(i));
                    } catch (SQLException e) {
                        log.warn("No books available");
                        rollback(connection);
                    }
                }
            }
            commit(connection);
        } catch (SQLException e) {
            rollback(connection);
            log.error(e);
        }
        setAutoCommit(connection, true);
        connectionPool.returnToPool(connection);
    }

    public Date getNearestAvailableDate(BookDto bookDto) {
        return recordDao.getNearestAvailableDateByBookId(bookDto.getId());
    }

    public List<RecordDto> getRecordsByBookId(int bookId) {
        return recordConverter.toDtos(recordDao.getRecordsByBookId(bookId));
    }

    public List<ReaderDto> getAllReaders() {
        return readerConverter.toDtos(readerDao.getAll());
    }

    private void scheduleJobs(RecordEntity recordEntity) {
        createDueDateReminder(recordEntity);
        createOverdueReminder(recordEntity);
    }

    private void unscheduleJobs(RecordDto recordDto) {
        scheduleManager.unscheduleJob(recordDto, DUE_DATE_REMINDER_STRING);
        scheduleManager.unscheduleJob(recordDto, OVERDUE_REMINDER_STRING);
    }

    private void createDueDateReminder(RecordEntity recordEntity) {
        Date triggerDate;
        //triggerDate = Date.valueOf(recordEntity.getDueDate().toLocalDate().minusWeeks(1).minusDays(1));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 6);
        triggerDate = new Date(calendar.getTime().getTime());
        scheduleManager.scheduleJob(recordEntity, DUE_DATE_REMINDER_STRING, triggerDate, DueDateReminderJob.class);
    }

    private void createOverdueReminder(RecordEntity recordEntity) {
        Date triggerDate = Date.valueOf(recordEntity.getDueDate().toLocalDate().plusDays(1));
        scheduleManager.scheduleJob(recordEntity, OVERDUE_REMINDER_STRING, triggerDate, OverdueReminderJob.class);
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


