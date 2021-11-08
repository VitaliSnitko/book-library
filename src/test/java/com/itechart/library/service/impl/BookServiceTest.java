package com.itechart.library.service.impl;

import com.itechart.library.dao.AuthorDao;
import com.itechart.library.dao.BookDao;
import com.itechart.library.dao.GenreDao;
import com.itechart.library.dao.criteria.BookSpecification;
import com.itechart.library.dao.impl.AuthorBookDaoImpl;
import com.itechart.library.dao.impl.GenreBookDaoImpl;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.entity.AuthorEntity;
import com.itechart.library.model.entity.BookEntity;
import com.itechart.library.model.entity.GenreEntity;
import com.itechart.library.service.BookService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private GenreDao genreDao;
    @Mock
    private AuthorBookDaoImpl authorBookDao;
    @Mock
    private GenreBookDaoImpl genreBookDao;

    private final BookSpecification specification = BookSpecification.builder().build();
    private final BookService bookService;
    private final List<BookEntity> bookEntities = new ArrayList<>();

    public BookServiceTest() {
        MockitoAnnotations.initMocks(this);
        bookService = new BookServiceImpl(bookDao, authorDao, genreDao, authorBookDao, genreBookDao);

        for (int i = 0; i < 15; i++) {
            this.bookEntities.add(BookEntity.builder().id(i).build());
        }
    }

    @Before
    public void setUpGiven() throws SQLException {
        //given
        when(authorDao.create(any(), any()))
                .thenReturn(createAuthor());
        when(genreDao.create(any(), any()))
                .thenReturn(createGenre());
    }

    @Test
    public void testThatBookCreateWasCalledOneTime() throws SQLException {
        //given
        BookDto bookDto = BookDto.builder().build();
        BookEntity bookEntity = createBookEntity();
        when(bookDao.create(any(), any()))
                .thenReturn(bookEntity);

        //when
        bookService.create(bookDto, null);

        //then
        verify(bookDao, times(1)).create(any(), any());
    }

    @Test
    public void testThatAuthorCreateWasCalledAsManyTimesAsSizeOfAuthorList() throws SQLException {
        //given
        BookDto bookDto = BookDto.builder().build();
        BookEntity bookEntity = createBookEntity();
        when(bookDao.create(any(), any()))
                .thenReturn(bookEntity);

        //when
        bookService.create(bookDto, null);

        //then
        verify(authorDao, times(bookEntity.getAuthorEntities().size())).create(any(), any());
    }

    @Test
    public void testThatGenreCreateWasCalledAsManyTimesAsSizeOfGenreList() throws SQLException {
        //given
        BookDto bookDto = BookDto.builder().build();
        BookEntity bookEntity = createBookEntity();
        when(bookDao.create(any(), any()))
                .thenReturn(bookEntity);

        //when
        bookService.create(bookDto, null);

        //then
        verify(genreDao, times(bookEntity.getGenreEntities().size())).create(any(), any());
    }

    @Test
    public void testThatBookUpdateWasCalledOneTime() throws SQLException {
        //given
        BookDto bookDto = BookDto.builder().build();
        BookEntity bookEntity = createBookEntity();
        when(bookDao.create(any(), any()))
                .thenReturn(bookEntity);

        //when
        bookService.update(bookDto, null);

        //then
        verify(bookDao, times(1)).create(any(), any());
    }

    private BookEntity createBookEntity() {
        return BookEntity.builder()
                .authorEntities(createAuthorsList())
                .genreEntities(createGenresList())
                .build();
    }

    private List<AuthorEntity> createAuthorsList() {
        List<AuthorEntity> authorEntities = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            authorEntities.add(AuthorEntity.builder().id(i).build());
        }
        return authorEntities;
    }

    private AuthorEntity createAuthor() {
        return AuthorEntity.builder().build();
    }

    private GenreEntity createGenre() {
        return GenreEntity.builder().build();
    }

    private List<GenreEntity> createGenresList() {
        List<GenreEntity> genreEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            genreEntities.add(GenreEntity.builder().id(i).build());
        }
        return genreEntities;
    }

    @Test
    public void testGetLimitOffsetBySpecificationWithMinParams() {
        //given
        final int testBookAmountOnOnePage = 5;
        final int testPage = 1;
        final int expectedLimit = 5;
        final int expectedOffset = 0;
        List<BookEntity> expectedList = this.bookEntities.subList(0, 1);
        when(bookDao.getLimitOffsetBySpecification(specification, expectedLimit, expectedOffset))
                .thenReturn(expectedList);

        //when
        bookService.getLimitOffsetBySpecification(specification, testBookAmountOnOnePage, testPage);

        //then
        verify(bookDao, times(1)).getLimitOffsetBySpecification(specification, expectedLimit, expectedOffset);
    }

    @Test
    public void testGetLimitOffsetBySpecificationWithMiddleParams() {
        //given
        int testBookAmountOnOnePage = 4;
        int testPage = 2;
        int expectedLimit = 4;
        int expectedOffset = 4;
        List<BookEntity> expectedList = this.bookEntities.subList(0, 1);
        when(bookDao.getLimitOffsetBySpecification(specification, expectedLimit, expectedOffset))
                .thenReturn(expectedList);

        //when
        bookService.getLimitOffsetBySpecification(specification, testBookAmountOnOnePage, testPage);

        //then
        verify(bookDao, times(1)).getLimitOffsetBySpecification(specification, expectedLimit, expectedOffset);
    }

    @Test
    public void testGetByIdWasCalledOneTime() {
        //given
        when(bookDao.getById(anyInt()))
                .thenReturn(Optional.empty());

        //when
        bookService.getById(anyInt());

        //then
        verify(bookDao, times(1)).getById(anyInt());
    }

    @Test
    public void testGetByIdIfBookNotFound() {
        //given
        when(bookDao.getById(anyInt()))
                .thenReturn(Optional.empty());

        //when
        BookDto bookDto = bookService.getById(anyInt());

        //then
        Assert.assertNotEquals(bookDto, null);
    }

    @Test
    public void testGetBookCountWasCalledOneTime() {
        //when
        bookService.getBookCount(any());

        //then
        verify(bookDao, times(1)).getCount(any());
    }

    @Test
    public void testDeleteWasCalledOneTime() {
        //given
        String[] ids = {};

        //when
        bookService.delete(ids);

        //then
        verify(bookDao, times(1)).delete(any());
    }

}