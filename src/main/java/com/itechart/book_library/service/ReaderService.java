package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.dao.api.ReaderDao;
import com.itechart.book_library.dao.impl.BookDaoImpl;
import com.itechart.book_library.dao.impl.ReaderDaoImpl;
import com.itechart.book_library.dao.impl.RecordDaoImpl;
import com.itechart.book_library.model.dto.*;
import com.itechart.book_library.model.entity.*;
import com.itechart.book_library.util.converter.Converter;
import com.itechart.book_library.util.converter.impl.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderService {

    private final ReaderDao readerDao;
    private final RecordDaoImpl recordDao = BaseDao.getDao(RecordDaoImpl.class);
    private final BookDao bookDao = BaseDao.getDao(BookDaoImpl.class);

    private final Converter<ReaderDto, ReaderEntity> readerConverter = new ReaderConverter();
    private final Converter<RecordDto, RecordEntity> recordConverter = new RecordConverter();

    private static ReaderService readerService;

    private ReaderService() {
        readerDao = BaseDao.getDao(ReaderDaoImpl.class);
    }

    public static ReaderService getInstance() {
        if (readerService == null) {
            readerService = new ReaderService();
        }
        return readerService;
    }

    public void addRecords(String[] emails, String[] names, String[] periods, int bookId) {
        List<ReaderEntity> readerEntities = getReaderEntities(emails, names);
        List<RecordEntity> recordEntities = getRecordEntities(readerEntities, periods, bookId);

        createReadersRecords(readerEntities, recordEntities);
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

    private void createReadersRecords(List<ReaderEntity> readerEntities, List<RecordEntity> recordEntities) {
        for (int i = 0; i < readerEntities.size(); i++) {
            Optional<ReaderEntity> readerByEmailOptional = readerDao.getByEmail(readerEntities.get(i).getEmail());
            ReaderEntity readerEntity;
            RecordEntity recordEntity = recordEntities.get(i);
            if (readerByEmailOptional.isEmpty()) {
                readerEntity = readerDao.create(readerEntities.get(i));
            } else {
                readerEntity = readerByEmailOptional.get();
            }
            recordEntity.getReader().setId(readerEntity.getId());

            if (readerDao.getByEmailInCurrentBook(readerEntity.getEmail(), recordEntities.get(i).getBookId()).isEmpty()) {
                recordDao.create(recordEntity);
                bookDao.takeBook(recordEntity.getBookId());
            }
        }
    }
}
