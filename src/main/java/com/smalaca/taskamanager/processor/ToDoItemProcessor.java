package com.smalaca.taskamanager.processor;

import com.google.common.collect.ImmutableMap;
import com.smalaca.taskamanager.events.EpicReadyToPrioritize;
import com.smalaca.taskamanager.exception.UnsupportedToDoItemType;
import com.smalaca.taskamanager.model.entities.Epic;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.enums.ToDoItemStatus;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.CommunicationService;
import com.smalaca.taskamanager.service.ProjectBacklogService;
import com.smalaca.taskamanager.service.SprintBacklogService;
import com.smalaca.taskamanager.service.StoryService;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.*;

@Component
public class ToDoItemProcessor {
    private final StoryService storyService;
    private final EventsRegistry eventsRegistry;
    private final ProjectBacklogService projectBacklogService;
    private final CommunicationService communicationService;
    private final SprintBacklogService sprintBacklogService;
    private final Map<ToDoItemStatus, ToDoItemState> states;

    public ToDoItemProcessor(
            StoryService storyService, EventsRegistry eventsRegistry, ProjectBacklogService projectBacklogService,
            CommunicationService communicationService, SprintBacklogService sprintBacklogService) {
        this.storyService = storyService;
        this.eventsRegistry = eventsRegistry;
        this.projectBacklogService = projectBacklogService;
        this.communicationService = communicationService;
        this.sprintBacklogService = sprintBacklogService;
        states = ImmutableMap.of(
                DONE, new DoneToDoItem(storyService, eventsRegistry),
                APPROVED, new ApprovedToDoItem(eventsRegistry, storyService),
                RELEASED, new ReleasedToDoItem(eventsRegistry),
                TO_BE_DEFINED, new ToBeDefinedToDoItem()
        );
    }

    public void processFor(ToDoItem toDoItem) {
        ToDoItemStatus status = toDoItem.getStatus();
        switch (status) {
            case DEFINED:
                processDefined(toDoItem);
                break;

            case IN_PROGRESS:
                processInProgress(toDoItem);
                break;

            default:
                states.get(status).process(toDoItem);
        }
    }

    private void processDefined(ToDoItem toDoItem) {
        if (toDoItem instanceof Story) {
            Story story = (Story) toDoItem;
            if (story.getTasks().isEmpty()) {
                projectBacklogService.moveToReadyForDevelopment(story, story.getProject());
            } else {
                if (!story.isAssigned()) {
                    communicationService.notifyTeamsAbout(story, story.getProject());
                }
            }
        } else {
            if (toDoItem instanceof Task) {
                Task task = (Task) toDoItem;
                sprintBacklogService.moveToReadyForDevelopment(task, task.getCurrentSprint());
            } else {
                if (toDoItem instanceof Epic) {
                    Epic epic = (Epic) toDoItem;
                    projectBacklogService.putOnTop(epic);
                    EpicReadyToPrioritize event = new EpicReadyToPrioritize();
                    event.setEpicId(epic.getId());
                    eventsRegistry.publish(event);
                    communicationService.notify(toDoItem, toDoItem.getProject().getProductOwner());
                } else {
                    throw new UnsupportedToDoItemType();
                }
            }
        }
    }

    private void processInProgress(ToDoItem toDoItem) {
        if (toDoItem instanceof Task) {
            Task task = (Task) toDoItem;
            storyService.updateProgressOf(task.getStory(), task);
        }
    }
}
