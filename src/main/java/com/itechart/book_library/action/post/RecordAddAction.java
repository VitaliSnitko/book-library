package com.itechart.book_library.action.post;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.service.ReaderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecordAddAction implements Action {

    ReaderService readerService = ReaderService.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        readerService.addReaderRecord(new ReaderDto(req), new RecordDto(req), Integer.parseInt(req.getParameter("bookId")));
        return "/";
    }
}
