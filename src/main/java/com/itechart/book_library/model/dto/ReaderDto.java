package com.itechart.book_library.model.dto;

import javax.servlet.http.HttpServletRequest;

public class ReaderDto {
    int id;
    String email;
    String name;

    public ReaderDto(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public ReaderDto(HttpServletRequest req) {
        this.email = req.getParameter("email");
        this.name = req.getParameter("name");
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
