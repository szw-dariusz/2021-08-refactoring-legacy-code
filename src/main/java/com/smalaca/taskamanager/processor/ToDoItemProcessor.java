package com.smalaca.taskamanager.processor;

import com.google.common.collect.ImmutableMap;
import com.smalaca.taskamanager.events.EpicReadyToPrioritize;
import com.smalaca.taskamanager.events.StoryApprovedEvent;
import com.smalaca.taskamanager.events.StoryDoneEvent;
import com.smalaca.taskamanager.events.TaskApprovedEvent;
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

import java.util.HashMap;
import java.util.Map;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.DONE;
import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.RELEASED;

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
                RELEASED, new ReleasedToDoItem(eventsRegistry)
        );
    }

    public void processFor(ToDoItem toDoItem) {
        switch (toDoItem.getStatus()) {
            case DEFINED:
                processDefined(toDoItem);
                break;

            case IN_PROGRESS:
                processInProgress(toDoItem);
                break;

            case DONE:
                processDone(toDoItem);
                break;

            case APPROVED:
                processApproved(toDoItem);
                break;

            case RELEASED:
                states.get(RELEASED).process(toDoItem);
                break;

            default:
                break;
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

    private void processDone(ToDoItem toDoItem) {
        if (toDoItem instanceof Task) {
            Task task = (Task) toDoItem;
            Story story = task.getStory();
            storyService.updateProgressOf(task.getStory(), task);
            if (DONE.equals(story.getStatus())) {
                StoryDoneEvent event = new StoryDoneEvent();
                event.setStoryId(story.getId());
                eventsRegistry.publish(event);
            }
        } else if (toDoItem instanceof Story) {
            Story story = (Story) toDoItem;
            StoryDoneEvent event = new StoryDoneEvent();
            event.setStoryId(story.getId());
            eventsRegistry.publish(event);
        }
    }

    private void processApproved(ToDoItem toDoItem) {
        if (toDoItem instanceof Story) {
            Story story = (Story) toDoItem;
            StoryApprovedEvent event = new StoryApprovedEvent();
            event.setStoryId(story.getId());
            eventsRegistry.publish(event);
        } else if (toDoItem instanceof Task) {
            Task task = (Task) toDoItem;

            if (task.isSubtask()) {
                TaskApprovedEvent event = new TaskApprovedEvent();
                event.setTaskId(task.getId());
                eventsRegistry.publish(event);
            } else {
                storyService.attachPartialApprovalFor(task.getStory().getId(), task.getId());
            }
        }
    }
}
