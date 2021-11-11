package com.itechart.library.servlet;

import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionFactory;
import com.itechart.library.servlet.action.ActionResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@code FrontController} is central controller designed to map HTTP requests
 * and actions through {@link ActionFactory}.
 */
@WebServlet({"/main", "/add", "/edit", "/delete", "/search", "/autocomplete"})
@MultipartConfig(maxFileSize = 2_097_152)
public class FrontController extends HttpServlet {

    private ActionFactory actionFactory;

    @Override
    public void init() {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
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
