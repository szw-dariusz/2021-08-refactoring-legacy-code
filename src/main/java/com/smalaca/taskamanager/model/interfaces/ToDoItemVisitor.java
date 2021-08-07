package com.smalaca.taskamanager.model.interfaces;

import com.smalaca.taskamanager.model.entities.*;

public interface ToDoItemVisitor {

	default void visit(Epic epic) {

	}

	default void visit(Story story) {

	}

	default void visit(Task task) {

	}
}
