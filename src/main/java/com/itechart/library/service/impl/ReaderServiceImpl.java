package com.itechart.library.service.impl;

import com.itechart.library.converter.BookConverter;
import com.itechart.library.converter.DtoRequestReaderConverter;
import com.itechart.library.converter.RecordConverter;
import com.itechart.library.converter.impl.BookConverterImpl;
import com.itechart.library.converter.impl.ReaderConverterImpl;
import com.itechart.library.converter.impl.RecordConverterImpl;
import com.itechart.library.dao.BookDao;
import com.itechart.library.dao.DaoFactory;
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
import com.itechart.library.scheduler.EmailScheduleManager;
import com.itechart.library.scheduler.job.DueDateReminderJob;
import com.itechart.library.scheduler.job.OverdueReminderJob;
import com.itechart.library.service.ReaderService;
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
public class ReaderServiceImpl implements ReaderService {

    private final EmailScheduleManager scheduleManager;

    private final ReaderDao readerDao;
    private final RecordDao recordDao;
    private final BookDao bookDao;

    private final BookConverter bookConverter = new BookConverterImpl();
    private final DtoRequestReaderConverter readerConverter = new ReaderConverterImpl();
    private final RecordConverter recordConverter = new RecordConverterImpl();

    private static final int DAYS_AFTER_DEADLINE = 1;
    private static final String DUE_DATE_REMINDER_STRING = "DueDateReminder";
    private static final String OVERDUE_REMINDER_STRING = "OverdueReminder";

    public ReaderServiceImpl() {
        this.bookDao = DaoFactory.getDao(BookDaoImpl.class);
        this.readerDao = DaoFactory.getDao(ReaderDaoImpl.class);
        this.recordDao = DaoFactory.getDao(RecordDaoImpl.class);
        this.scheduleManager = EmailScheduleManager.getInstance();
    }

    public ReaderServiceImpl(BookDao bookDao, ReaderDao readerDao, RecordDao recordDao, EmailScheduleManager scheduleManager) {
        this.bookDao = bookDao;
        this.readerDao = readerDao;
        this.recordDao = recordDao;
        this.scheduleManager = scheduleManager;
    }

    @Override
    public void createReaderRecords(String[] emails, String[] names, String[] periods, BookDto bookDto, Connection connection)
            throws SQLException {
        List<ReaderEntity> readerEntities = getReaderEntities(emails, names);
        List<RecordEntity> recordEntities = getRecordEntities(readerEntities, periods, bookDto);
        createReaderRecords(readerEntities, recordEntities, connection);
    }

    @Override
    public void updateRecords(List<RecordDto> recordDtos, Connection connection) throws SQLException {
        if (recordDtos.isEmpty()) {
            return;
        }
        int bookId = recordDtos.get(0).getBook().getId();
        int availableBookAmount = bookDao.getAvailableBookCount(bookId);
        int totalBookAmount = bookDao.getTotalBookAmount(bookId);
        for (RecordDto recordDto : recordDtos) {
            if (recordDto.getStatus() == Status.RETURNED) {
                availableBookAmount++;
            } else {
                totalBookAmount--;
            }
            updateRecord(connection, recordDto);
            unscheduleJobs(recordDto);
        }
        bookDao.updateAvailableAmount(availableBookAmount, bookId, connection);
        bookDao.updateTotalAmount(totalBookAmount, bookId, connection);
    }

    @Override
    public Date getNearestAvailableDate(BookDto bookDto) {
        return recordDao.getNearestAvailableDateByBookId(bookDto.getId());
    }

    @Override
    public List<RecordDto> getRecordsByBookId(int bookId) {
        return recordConverter.toDtos(recordDao.getRecordsByBookId(bookId));
    }

    @Override
    public List<ReaderDto> getAllReaders() {
        return readerConverter.toDtos(readerDao.getAll());
    }

    @Override
    public List<ReaderDto> getReadersByEmailInput(String email) {
        return readerConverter.toDtos(readerDao.getByEmailInput(email));
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

    private void createReaderRecords(List<ReaderEntity> readerEntities, List<RecordEntity> recordEntities, Connection connection) throws SQLException {
        if (recordEntities.isEmpty()) {
            return;
        }
        int bookId = recordEntities.get(0).getBook().getId();
        int availableBookAmount = bookDao.getAvailableBookCount(bookId);
        for (int i = 0; i < readerEntities.size(); i++) {
            RecordEntity recordEntity = recordEntities.get(i);
            Optional<ReaderEntity> readerByEmailOptional = readerDao.getByEmail(readerEntities.get(i).getEmail(), connection);
            ReaderEntity newReaderEntity = createOrUpdateReader(readerEntities.get(i), readerByEmailOptional, connection);
            recordEntity.getReader().setId(newReaderEntity.getId());

            Optional<RecordEntity> recordOptional = recordDao.getByEmailInCurrentBook(newReaderEntity.getEmail(), recordEntity.getBook().getId(), connection);
            if (recordOptional.isEmpty() || recordOptional.get().getStatus() != Status.BORROWED) {
                recordDao.create(recordEntity, connection);
                availableBookAmount--;
                bookDao.updateAvailableAmount(availableBookAmount, bookId, connection);
                scheduleJobs(recordEntity);
            }
        }
    }

    private ReaderEntity createOrUpdateReader(ReaderEntity readerEntity, Optional<ReaderEntity> readerByEmailOptional, Connection connection) throws SQLException {
        ReaderEntity newReaderEntity;
        if (readerByEmailOptional.isEmpty()) {
            newReaderEntity = readerDao.create(readerEntity, connection);
        } else {
            readerDao.update(readerEntity, connection);
            newReaderEntity = readerByEmailOptional.get();
        }
        return newReaderEntity;
    }

    private void scheduleJobs(RecordEntity recordEntity) {
        createDueDateReminder(recordEntity);
        createOverdueReminder(recordEntity);
    }

    private void unscheduleJobs(RecordDto recordDto) {
        scheduleManager.unsubscribe(recordDto, DUE_DATE_REMINDER_STRING);
        scheduleManager.unsubscribe(recordDto, OVERDUE_REMINDER_STRING);
    }

    private void createDueDateReminder(RecordEntity recordEntity) {
        Date triggerDate;
        //triggerDate = Date.valueOf(recordEntity.getDueDate().toLocalDate().minusWeeks(1).minusDays(1));
        triggerDate = getDate();
        scheduleManager.subscribe(recordEntity, DUE_DATE_REMINDER_STRING, triggerDate, DueDateReminderJob.class);
    }

    //this method is only for manual testing
    private Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 30);
        return new Date(calendar.getTime().getTime());
    }

    private void createOverdueReminder(RecordEntity recordEntity) {
        Date triggerDate = Date.valueOf(recordEntity.getDueDate().toLocalDate().plusDays(DAYS_AFTER_DEADLINE));
        scheduleManager.subscribe(recordEntity, OVERDUE_REMINDER_STRING, triggerDate, OverdueReminderJob.class);
    }
}


