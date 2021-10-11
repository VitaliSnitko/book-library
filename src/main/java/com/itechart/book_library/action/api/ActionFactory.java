package com.itechart.book_library.action.api;

import com.itechart.book_library.action.get.BookAddPageAction;
import com.itechart.book_library.action.get.BookEditPageAction;
import com.itechart.book_library.action.get.MainPageAction;
import com.itechart.book_library.action.post.BookAddAction;
import com.itechart.book_library.action.post.BookEditAction;
import com.itechart.book_library.connection.ConnectionPool;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private final Map<String, Action> actions;
    private static ActionFactory actionFactory;

    private ActionFactory() {
        actions = new HashMap<>();

        actions.put("GET", new MainPageAction());
        actions.put("GET/add", new BookAddPageAction());
        actions.put("GET/edit", new BookEditPageAction());

        actions.put("POST/add", new BookAddAction());
        actions.put("POST/edit", new BookEditAction());
    }

    public static ActionFactory getInstance() {
        if (actionFactory == null)
            actionFactory = new ActionFactory();
        return actionFactory;
    }

    public Action getAction(HttpServletRequest req) {
        return actions.get(req.getMethod() + req.getServletPath());
    }
}
