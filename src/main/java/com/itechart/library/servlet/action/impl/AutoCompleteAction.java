package com.itechart.library.servlet.action.impl;

import com.google.gson.Gson;
import com.itechart.library.model.dto.ReaderDto;
import com.itechart.library.service.impl.ReaderServiceProxy;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionResult;
import com.itechart.library.servlet.action.OperationAfterAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AutoCompleteAction implements Action {

    private final ReaderServiceProxy readerService = new ReaderServiceProxy();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ReaderDto> readers = readerService.getReadersByEmailInput(req.getParameter("input"));
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(readers));
        return new ActionResult(OperationAfterAction.NONE);
    }
}
