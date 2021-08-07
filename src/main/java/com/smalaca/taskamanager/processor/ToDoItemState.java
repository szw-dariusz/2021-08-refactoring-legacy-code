package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.model.interfaces.ToDoItem;

interface ToDoItemState {
    void process(ToDoItem toDoItem);
}
