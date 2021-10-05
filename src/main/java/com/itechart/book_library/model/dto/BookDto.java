package com.itechart.book_library.model.dto;

import com.itechart.book_library.model.entity.Author;
import com.itechart.book_library.model.entity.Book;
import com.itechart.book_library.model.entity.Genre;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BookDto {

    private String title;
    String[] authors;
    String[] genres;
    private String publisher;
    private Date publishDate;
    private int pageCount;
    private String ISBN;
    private String description;
    private InputStream cover;

    public BookDto(Book book, List<Author> authorList, List<Genre> genreList) {
        title       = book.getTitle();
        authors     = authorList.stream().map(Author::getName).toArray(String[]::new);
        genres      = genreList.stream().map(Genre::getName).toArray(String[]::new);
        publisher   = book.getPublisher();
        publishDate = book.getPublishDate();
        pageCount   = book.getPageCount();
        ISBN        = book.getISBN();
        description = book.getDescription();
        cover       = book.getCover();
    }

    public BookDto(HttpServletRequest req) throws ServletException, IOException {
        title       = req.getParameter("title");
        authors     = req.getParameter("authors").split(" *, *");
        genres      = req.getParameter("genres").split(" *, *");
        publisher   = req.getParameter("publisher");
        publishDate = getDate(req.getParameter("date"));
        pageCount   = Integer.parseInt(req.getParameter("page count"));
        ISBN        = req.getParameter("ISBN");
        description = req.getParameter("description");
        cover       = req.getPart("cover").getInputStream();
    }

    public Book convertBookDtoToBookEntity() {
        Book book = new Book();
        book.setTitle(title);
        book.setPublisher(publisher);
        book.setPublishDate(publishDate);
        book.setPageCount(pageCount);
        book.setISBN(ISBN);
        book.setDescription(description);
        book.setCover(cover);
        return book;
    }

    public List<Author> getListOfAuthors() {
        List<Author> listOfAuthors = new ArrayList<>();
        for (String authorName : authors) {
            listOfAuthors.add(new Author(authorName));
        }
        return listOfAuthors;
    }

    public List<Genre> getListOfGenres() {
        List<Genre> listOfGenres = new ArrayList<>();
        for (String genreName : genres) {
            listOfGenres.add(new Genre(genreName));
        }
        return listOfGenres;
    }

    private Date getDate(String stringDate) {
        java.util.Date utilDate = null;
        try {
            utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(utilDate.getTime());
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getDescription() {
        return description;
    }

    public InputStream getCover() {
        return cover;
    }
}
