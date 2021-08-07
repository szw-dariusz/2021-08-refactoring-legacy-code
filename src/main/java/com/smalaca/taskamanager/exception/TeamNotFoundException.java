package com.smalaca.taskamanager.exception;

public class TeamNotFoundException extends RuntimeException {
    private Long id;

    public TeamNotFoundException(Long id) {
        this();
        this.id = id;
    }

    public TeamNotFoundException() {
        super();
    }
}
