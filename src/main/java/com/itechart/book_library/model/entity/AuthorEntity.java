package com.itechart.book_library.model.entity;

public class AuthorEntity extends Entity{
    private String name;

    public AuthorEntity() {
    }

    public AuthorEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
