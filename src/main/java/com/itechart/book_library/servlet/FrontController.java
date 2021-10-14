package com.itechart.book_library.servlet;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.action.api.ActionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/main", "/add", "/edit", "/delete"})
@MultipartConfig
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

    private void doForwardOrRedirect(String viewName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("GET")) {
            String path = "/WEB-INF/jsp/" + viewName + ".jsp";
            req.getRequestDispatcher(path).forward(req, resp);
        } else {
            resp.sendRedirect(viewName);
        }
    }
}
