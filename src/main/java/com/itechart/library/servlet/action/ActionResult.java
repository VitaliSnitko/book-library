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
    private String path;

    /**
     * Assume enum values {@link OperationAfterAction} where REDIRECT means that user will be redirected to path,
     * FORWARD - forwarded, NONE - user would be neither redirected nor forwarded. FORWARD is default value
     * {@code true} value for redirecting, {@code false} value for forwarding
     */
    private OperationAfterAction operationAfterAction = OperationAfterAction.FORWARD;

    public ActionResult(String path, OperationAfterAction operationAfterAction) {
        this.path = path;
        this.operationAfterAction = operationAfterAction;
    }

    public ActionResult(OperationAfterAction operationAfterAction) {
        this.operationAfterAction = operationAfterAction;
    }

    public ActionResult(String path) {
        this.path = path;
    }
}
