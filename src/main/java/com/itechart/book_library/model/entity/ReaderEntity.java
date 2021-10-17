package com.itechart.book_library.model.entity;

public class ReaderEntity extends Entity {
    String email;
    String name;

    public ReaderEntity() {
    }

    public ReaderEntity(int id, String email, String name) {
        super(id);
        this.email = email;
        this.name = name;
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
