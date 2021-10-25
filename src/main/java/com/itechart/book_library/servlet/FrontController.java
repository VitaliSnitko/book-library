package com.itechart.book_library.servlet;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.action.api.ActionFactory;
import com.itechart.book_library.action.api.ActionResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/main", "/add", "/edit", "/delete", "/search"})
@MultipartConfig(maxFileSize = 2097152)
public class FrontController extends HttpServlet {

    ActionFactory actionFactory;

    @Override
    public void init() {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Action action = actionFactory.getAction(req);
        doForwardOrRedirect(action.execute(req, resp), req, resp);
    }

    private void doForwardOrRedirect(ActionResult actionResult, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (actionResult.isRedirect()) {
            resp.sendRedirect(actionResult.getPath());
        } else {
            String path = "/WEB-INF/jsp/" + actionResult.getPath() + ".jsp";
            req.getRequestDispatcher(path).forward(req, resp);
        }
    }
}
