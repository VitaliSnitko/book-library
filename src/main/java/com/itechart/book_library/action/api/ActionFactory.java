package com.itechart.book_library.action.api;

import com.itechart.book_library.action.get.BookAddPageAction;
import com.itechart.book_library.action.get.BookEditPageAction;
import com.itechart.book_library.action.get.BookListAction;
import com.itechart.book_library.action.post.BookAddAction;
import com.itechart.book_library.action.post.BookDeleteAction;
import com.itechart.book_library.action.post.BookEditAction;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private final Map<String, Action> actionPerURL;
    private static ActionFactory actionFactory;

    private ActionFactory() {
        actionPerURL = new HashMap<>();

        actionPerURL.put("GET/main", new BookListAction());
        actionPerURL.put("GET/add", new BookAddPageAction());
        actionPerURL.put("GET/edit", new BookEditPageAction());

        actionPerURL.put("POST/add", new BookAddAction());
        actionPerURL.put("POST/edit", new BookEditAction());
        actionPerURL.put("POST/delete", new BookDeleteAction());
    }

    public static ActionFactory getInstance() {
        if (actionFactory == null)
            actionFactory = new ActionFactory();
        return actionFactory;
    }

    public Action getAction(HttpServletRequest req) {
        return actionPerURL.get(req.getMethod() + req.getServletPath());
    }
}
