package com.smalaca.taskamanager.processor;

import com.google.common.collect.ImmutableMap;
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

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.*;

@Component
public class ToDoItemProcessor {
    private final Map<ToDoItemStatus, ToDoItemState> states;

    public ToDoItemProcessor(
            StoryService storyService, EventsRegistry eventsRegistry, ProjectBacklogService projectBacklogService,
            CommunicationService communicationService, SprintBacklogService sprintBacklogService) {
        states = new HashMap<>();
        states.put(DEFINED, new DefinedToDoItem(communicationService, projectBacklogService, sprintBacklogService, eventsRegistry));
        states.put(IN_PROGRESS, new InProgressToDoItem(storyService));
        states.put(DONE, new DoneToDoItem(storyService, eventsRegistry));
        states.put(APPROVED, new ApprovedToDoItem(eventsRegistry, storyService));
        states.put(RELEASED, new ReleasedToDoItem(eventsRegistry));
        states.put(TO_BE_DEFINED, new ToBeDefinedToDoItem());
    }

    public void processFor(ToDoItem toDoItem) {
        states.get(toDoItem.getStatus()).process(toDoItem);
    }
}
