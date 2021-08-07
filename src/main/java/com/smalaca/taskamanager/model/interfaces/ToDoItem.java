package com.smalaca.taskamanager.model.interfaces;

import java.util.List;

import com.smalaca.taskamanager.model.embedded.*;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.enums.ToDoItemStatus;

public interface ToDoItem {
    ToDoItemStatus getStatus();

    Project getProject();

    List<Watcher> getWatchers();

    Owner getOwner();

    boolean isAssigned();

    Assignee getAssignee();

    List<Stakeholder> getStakeholders();

    Long getId();

    void accept(ToDoItemVisitor visitor);
}
