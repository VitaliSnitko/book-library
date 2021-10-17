package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.dao.api.ReaderDao;
import com.itechart.book_library.dao.impl.*;
import com.itechart.book_library.model.dto.*;
import com.itechart.book_library.model.entity.*;
import com.itechart.book_library.util.converter.Converter;
import com.itechart.book_library.util.converter.impl.*;

import java.util.List;
import java.util.Optional;

public class ReaderService extends Service {

    private static ReaderDao readerDao = getDao(ReaderDaoImpl.class);
    private static RecordDaoImpl recordDao = getDao(RecordDaoImpl.class);
    private static BookDao bookDao = getDao(BookDaoImpl.class);

    private static Converter<ReaderDto, ReaderEntity> readerConverter = new ReaderConverter();
    private static Converter<RecordDto, RecordEntity> recordConverter = new RecordConverter();
    private static ReaderService readerService;

    private ReaderService() {
    }

    public static ReaderService getInstance() {
        if (readerService == null)
            readerService = new ReaderService();
        return readerService;
    }

    public void addReaderRecord(ReaderDto readerDto, RecordDto recordDto, int bookId) {
        Optional<ReaderEntity> readerOptional = readerDao.getByEmail(readerDto.getEmail());
        if (readerOptional.isEmpty()) {
            ReaderEntity readerEntity = readerConverter.toEntity(readerDto);
            readerDao.create(readerEntity);
            recordDto.setReader(readerConverter.toDto(readerEntity));
            recordDao.create(recordConverter.toEntity(recordDto));
            bookDao.takeBook(bookId);
        } else {
            readerOptional.get().setName(readerDto.getName());
            readerDao.update(readerOptional.get());
        }

    }

    public List<RecordDto> getRecords(int bookId) {
        return recordConverter.toDtos(recordDao.getRecords(bookId));
    }
}
