package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.EpicReadyToPrioritize;
import com.smalaca.taskamanager.exception.UnsupportedToDoItemType;
import com.smalaca.taskamanager.model.entities.Epic;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.interfaces.ToDoItemVisitor;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.CommunicationService;
import com.smalaca.taskamanager.service.ProjectBacklogService;
import com.smalaca.taskamanager.service.SprintBacklogService;

class DefinedToDoItemVisitor implements ToDoItemVisitor {
    private final CommunicationService communicationService;
    private final ProjectBacklogService projectBacklogService;
    private final SprintBacklogService sprintBacklogService;
    private final EventsRegistry eventsRegistry;

    DefinedToDoItemVisitor(
            CommunicationService communicationService, ProjectBacklogService projectBacklogService, SprintBacklogService sprintBacklogService, EventsRegistry eventsRegistry) {
        this.communicationService = communicationService;
        this.projectBacklogService = projectBacklogService;
        this.sprintBacklogService = sprintBacklogService;
        this.eventsRegistry = eventsRegistry;
    }

    @Override
    public void visit(Task task) {
        sprintBacklogService.moveToReadyForDevelopment(task, task.getCurrentSprint());
    }

    @Override
    public void visit(Story story) {
        if (story.getTasks().isEmpty()) {
            projectBacklogService.moveToReadyForDevelopment(story, story.getProject());
        } else {
            if (!story.isAssigned()) {
                communicationService.notifyTeamsAbout(story, story.getProject());
            }
        }
    }

    @Override
    public void visit(Epic epic) {
        projectBacklogService.putOnTop(epic);
        EpicReadyToPrioritize event = new EpicReadyToPrioritize();
        event.setEpicId(epic.getId());
        eventsRegistry.publish(event);
        communicationService.notify(epic, epic.getProject().getProductOwner());
    }
}
