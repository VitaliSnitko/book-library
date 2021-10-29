package com.itechart.library.servlet.action;

public class ActionResult {

    private final String path;
    private boolean redirect;

    public ActionResult(String path, boolean redirect) {
        this.path = path;
        this.redirect = redirect;
    }

    public ActionResult(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean isRedirect() {
        return redirect;
    }
}
