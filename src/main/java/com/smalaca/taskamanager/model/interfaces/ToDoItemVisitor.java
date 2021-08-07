package com.smalaca.taskamanager.model.interfaces;

import com.smalaca.taskamanager.model.entities.*;

public interface ToDoItemVisitor {

	void visit(Epic epic);

	void visit(Story story);

	void visit(Task task);
}
