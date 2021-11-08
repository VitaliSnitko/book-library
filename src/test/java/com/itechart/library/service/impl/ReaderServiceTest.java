package com.itechart.library.service.impl;

import com.itechart.library.dao.BookDao;
import com.itechart.library.dao.ReaderDao;
import com.itechart.library.dao.RecordDao;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.entity.BookEntity;
import com.itechart.library.model.entity.ReaderEntity;
import com.itechart.library.model.entity.RecordEntity;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReaderServiceTest {

    @Mock
    private ReaderDao readerDao;
    @Mock
    private RecordDao recordDao;
    @Mock
    private BookDao bookDao;
    @Mock
    private ReaderServiceImpl readerService;
    private List<ReaderEntity> readerEntities = new ArrayList<>();
    private List<RecordEntity> recordEntities = new ArrayList<>();

    public ReaderServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.readerService = new ReaderServiceImpl(bookDao, readerDao, recordDao);
        for (int i = 0; i < 5; i++) {
            readerEntities.add(ReaderEntity.builder().id(i).build());
        }
        for (int i = 0; i < 5; i++) {
            recordEntities.add(RecordEntity.builder().id(i).book(new BookEntity()).reader(readerEntities.get(i)).build());
        }
    }

//    @Test
//    public void testRecordCreateWasCalledAsManyTimesAsRecordListSize() throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method createMethod = ReaderServiceImpl.class.getDeclaredMethod("createReaderRecords", List.class, List.class, Connection.class);
//        createMethod.setAccessible(true);
//        when(readerDao.create(any(), any()))
//                .thenReturn(new ReaderEntity());
//        doNothing().when(readerService).scheduleJobs(any());
//        createMethod.invoke(readerService, readerEntities, recordEntities, null);
//
//        verify(recordDao, times(readerEntities.size())).create(any(), any());
//    }

    @Test
    public void testGetNearestAvailableDateWasCalledOneTime() {
        //when
        readerService.getNearestAvailableDate(BookDto.builder().id(1).build());

        //then
        verify(recordDao, times(1)).getNearestAvailableDateByBookId(anyInt());
    }

    @Test
    public void testGetRecordsByBookIdWasCalledOneTime() {
        //when
        readerService.getRecordsByBookId(anyInt());

        //then
        verify(recordDao, times(1)).getRecordsByBookId(anyInt());
    }

    @Test
    public void getAllReaders() {
        //when
        readerService.getAllReaders();

        //then
        verify(readerDao, times(1)).getAll();
    }
}