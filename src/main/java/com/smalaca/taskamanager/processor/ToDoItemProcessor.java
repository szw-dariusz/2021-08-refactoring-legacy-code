package com.smalaca.taskamanager.processor;

import java.util.Map;

import com.smalaca.taskamanager.model.enums.ToDoItemStatus;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.*;
import org.springframework.stereotype.Component;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.*;

@Component
public class ToDoItemProcessor {

    private final Map<ToDoItemStatus, ToDoItemState> states;

    public ToDoItemProcessor(StoryService storyService,
                             EventsRegistry eventsRegistry,
                             ProjectBacklogService projectBacklogService,
                             CommunicationService communicationService,
                             SprintBacklogService sprintBacklogService) {
        this.states = Map.ofEntries(Map.entry(RELEASED, new ReleasedToDoItem(eventsRegistry)),
                                    Map.entry(TO_BE_DEFINED, new ToBeDefinedItem()),
                                    Map.entry(DEFINED,
                                              new DefinedItemState(projectBacklogService,
                                                                   communicationService,
                                                                   sprintBacklogService,
                                                                   eventsRegistry)),
                                    Map.entry(APPROVED, new ApprovedItemState(storyService, eventsRegistry)),
                                    Map.entry(DONE, new DoneItemStateImpl(storyService, eventsRegistry)),
                                    Map.entry(IN_PROGRESS, new InProgressItemState(storyService)));
    }

    public void processFor(ToDoItem toDoItem) {
        states.get(toDoItem.getStatus()).process(toDoItem);
    }
}
