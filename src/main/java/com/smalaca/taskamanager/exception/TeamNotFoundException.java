package com.smalaca.taskamanager.exception;

public class TeamNotFoundException extends RuntimeException {

	private final long id;

	public TeamNotFoundException() {
		this.id = 0;
	}

	public TeamNotFoundException(final long id) {
		this.id = id;
	}
}
