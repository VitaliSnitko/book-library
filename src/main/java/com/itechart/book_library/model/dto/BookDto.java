package com.itechart.book_library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private int id;
    private String title;
    List<AuthorDto> authorDtos;
    List<GenreDto> genreDtos;
    private String publisher;
    private Date publishDate;
    private int pageCount;
    private String ISBN;
    private String description;
    private InputStream cover;
    private String base64Cover;
    private int availableBookAmount;
    private int totalBookAmount;

    public BookDto(int id, String title, List<AuthorDto> authorDtos, List<GenreDto> genreDtos,
                   String publisher, Date publishDate, int pageCount, String ISBN, String description,
                   InputStream cover, int availableBookAmount, int totalBookAmount) {
        this.id = id;
        this.title = title;
        this.authorDtos = authorDtos;
        this.genreDtos = genreDtos;

        this.publisher = publisher;
        this.publishDate = publishDate;
        this.pageCount = pageCount;
        this.ISBN = ISBN;
        this.description = description;
        this.cover = cover;
        try {
            base64Cover = (cover == null) ? "" : Base64.getEncoder().encodeToString(IOUtils.toByteArray(cover));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.availableBookAmount = availableBookAmount;
        this.totalBookAmount = totalBookAmount;
    }

    public BookDto(HttpServletRequest req) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            this.id = Integer.parseInt(req.getParameter("id"));
        }
        this.title = req.getParameter("title");
        this.authorDtos = Arrays.stream(req.getParameter("authors").split(" *, *"))
                .map(AuthorDto::new)
                .collect(Collectors.toList());
        this.genreDtos = Arrays.stream(req.getParameter("genres").split(" *, *"))
                .map(GenreDto::new)
                .collect(Collectors.toList());
        this.publisher = req.getParameter("publisher");
        this.publishDate = getDate(req.getParameter("date"));
        this.pageCount = Integer.parseInt(req.getParameter("pageCount"));
        this.ISBN = req.getParameter("ISBN");
        this.description = req.getParameter("description");
        this.cover = req.getPart("cover").getInputStream();
        if (cover.available() == 0) {
            cover = null;
        }
        this.totalBookAmount = Integer.parseInt(req.getParameter("totalBookAmount"));
        this.availableBookAmount = this.totalBookAmount;
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

    public List<AuthorDto> getAuthorDtos() {
        return authorDtos;
    }

    public List<GenreDto> getGenreDtos() {
        return genreDtos;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public String getBase64Cover() {
        return base64Cover;
    }

    public int getAvailableBookAmount() {
        return availableBookAmount;
    }

    public int getTotalBookAmount() {
        return totalBookAmount;
    }
}
