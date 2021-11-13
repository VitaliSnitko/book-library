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

    private static final String UTF_8 = "UTF-8";
    private ActionFactory actionFactory;

    @Override
    public void init() {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(UTF_8);
        Action action = actionFactory.getAction(req);
        doOperationAfterAction(action.execute(req, resp), req, resp);
    }

    private void doOperationAfterAction(ActionResult actionResult, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (actionResult.getOperationAfterAction()) {
            case REDIRECT -> resp.sendRedirect(actionResult.getPath());
            case FORWARD -> {
                String path = "/WEB-INF/jsp/" + actionResult.getPath() + ".jsp";
                req.getRequestDispatcher(path).forward(req, resp);
            }
            default -> {}
        }
    }
}
