package com.itechart.library.model.entity;

public enum Status {
    BORROWED("borrowed"),
    RETURNED("returned"),
    RETURNED_AND_DAMAGED("returned and damaged"),
    LOST("lost");

    public final String status;

    Status(String status) {
        this.status = status;
    }

}
