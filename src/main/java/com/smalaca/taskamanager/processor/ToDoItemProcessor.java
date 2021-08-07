package com.smalaca.taskamanager.processor;

import java.util.*;

import com.smalaca.taskamanager.events.*;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.enums.ToDoItemStatus;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.*;
import org.springframework.stereotype.Component;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.DEFINED;
import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.DONE;
import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.RELEASED;
import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.TO_BE_DEFINED;

@Component
public class ToDoItemProcessor {
    private final StoryService storyService;
    private final EventsRegistry eventsRegistry;
    private final Map<ToDoItemStatus, ToDoItemState> states;

    public ToDoItemProcessor(
            StoryService storyService, EventsRegistry eventsRegistry, ProjectBacklogService projectBacklogService,
            CommunicationService communicationService, SprintBacklogService sprintBacklogService) {
        this.storyService = storyService;
        this.eventsRegistry = eventsRegistry;
        var states = new EnumMap<ToDoItemStatus, ToDoItemState>(ToDoItemStatus.class);
        states.put(RELEASED, new ReleasedToDoItem(eventsRegistry));
        states.put(TO_BE_DEFINED, new ToBeDefinedItem());
        states.put(DEFINED,
                   new DefinedItemState(projectBacklogService, communicationService, sprintBacklogService, eventsRegistry));
        this.states = Collections.unmodifiableMap(states);
    }

    public void processFor(ToDoItem toDoItem) {
        var status = toDoItem.getStatus();
        switch (status) {
            case IN_PROGRESS:
                processInProgress(toDoItem);
                return;

            case DONE:
                processDone(toDoItem);
                return;

            case APPROVED:
                processApproved(toDoItem);
                return;
        }
        states.get(status).process(toDoItem);
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
