package com.smalaca.taskamanager.model.interfaces;

import com.smalaca.taskamanager.model.entities.Epic;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;

public interface ToDoItemVisitor {
    void visit(Task task);

    void visit(Story story);

    void visit(Epic epic);
}
