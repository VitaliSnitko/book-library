package com.itechart.library.servlet.action;

import lombok.Getter;

/**
 * Contains information about post-action redirecting or forwarding
 */
@Getter
public class ActionResult {

    /**
     * Relative path that will be reached after redirecting or forwarding
     */
    private final String path;

    /**
     * Tells whether path will be reached by redirecting or forwarding.
     * {@code true} value for redirecting, {@code false} value for forwarding
     */
    private boolean redirect;

    public ActionResult(String path, boolean redirect) {
        this.path = path;
        this.redirect = redirect;
    }

    public ActionResult(String path) {
        this.path = path;
    }

}
