package com.itechart.library.servlet.action;

import com.itechart.library.servlet.action.impl.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps HTTP request and action
 */
public class ActionFactory {

    /**
     * HashMap that contains key in format "[HTTP method]/[Relative path to resource]"
     * and a value of {@link Action} implementation
     */
    private final Map<String, Action> actionPerURL;

    private static volatile ActionFactory instance;

    private ActionFactory() {
        actionPerURL = new HashMap<>();

        actionPerURL.put("GET/main", new BookListAction());
        actionPerURL.put("GET/add", new BookAddPageAction());
        actionPerURL.put("GET/edit", new BookPageAction());
        actionPerURL.put("GET/search", new SearchPageAction());
        actionPerURL.put("GET/autocomplete", new AutoCompleteAction());

        actionPerURL.put("POST/add", new BookAddAction());
        actionPerURL.put("POST/edit", new BookEditAction());
        actionPerURL.put("POST/delete", new BookDeleteAction());
    }

    public static ActionFactory getInstance() {
        ActionFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (ActionFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ActionFactory();
                }
            }
        }
        return localInstance;
    }

    public Action getAction(HttpServletRequest req) {
        return actionPerURL.get(req.getMethod() + req.getServletPath());
    }
}
