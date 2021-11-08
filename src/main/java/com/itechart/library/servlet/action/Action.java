package com.itechart.library.servlet.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {

    /**
     * Main action performed by the {@code Action} class
     * @return {@link ActionResult} class that contains information about post-action redirecting or forwarding
     */
    ActionResult execute(HttpServletRequest req, HttpServletResponse resp);
}
