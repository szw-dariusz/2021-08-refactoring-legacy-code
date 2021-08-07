package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.EpicReadyToPrioritize;
import com.smalaca.taskamanager.exception.UnsupportedToDoItemType;
import com.smalaca.taskamanager.model.entities.Epic;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.model.interfaces.ToDoItemVisitor;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.CommunicationService;
import com.smalaca.taskamanager.service.ProjectBacklogService;
import com.smalaca.taskamanager.service.SprintBacklogService;

public class DefinedToDoItem implements ToDoItemState {
    private final CommunicationService communicationService;
    private final ProjectBacklogService projectBacklogService;
    private final SprintBacklogService sprintBacklogService;
    private final EventsRegistry eventsRegistry;

    DefinedToDoItem(
            CommunicationService communicationService, ProjectBacklogService projectBacklogService,
            SprintBacklogService sprintBacklogService, EventsRegistry eventsRegistry) {
        this.communicationService = communicationService;
        this.projectBacklogService = projectBacklogService;
        this.sprintBacklogService = sprintBacklogService;
        this.eventsRegistry = eventsRegistry;
    }

    @Override
    public void process(ToDoItem toDoItem) {
        ToDoItemVisitor visitor = new DefinedToDoItemVisitor(communicationService, projectBacklogService, sprintBacklogService, eventsRegistry);
        toDoItem.accept(visitor);
    }
}
