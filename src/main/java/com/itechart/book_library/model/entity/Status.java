package com.itechart.book_library.model.entity;

public enum Status {
    RETURNED("returned"),
    RETURNED_AND_DAMAGED("returned and damaged"),
    LOST("lost");

    public final String status;

    Status(String status) {
        this.status = status;
    }

}
