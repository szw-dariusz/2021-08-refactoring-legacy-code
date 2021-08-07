package com.smalaca.taskamanager.processor;

import java.util.Map;

import com.smalaca.taskamanager.model.enums.ToDoItemStatus;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.model.interfaces.ToDoItemVisitor;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.*;
import org.springframework.stereotype.Component;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.*;

@Component
public class ToDoItemProcessor {

    private final Map<ToDoItemStatus, ToDoItemVisitor> visitors;

    public ToDoItemProcessor(StoryService storyService,
                             EventsRegistry eventsRegistry,
                             ProjectBacklogService projectBacklogService,
                             CommunicationService communicationService,
                             SprintBacklogService sprintBacklogService) {
        this.visitors = Map.ofEntries(Map.entry(RELEASED, new ReleasedItemVisitor(eventsRegistry)),
                                      Map.entry(TO_BE_DEFINED, new ToBeDefinedItemVisitor()),
                                      Map.entry(DEFINED,
                                                new DefinedItemVisitor(projectBacklogService,
                                                                       communicationService,
                                                                       sprintBacklogService,
                                                                       eventsRegistry)),
                                      Map.entry(APPROVED, new ApprovedItemVisitor(storyService, eventsRegistry)),
                                      Map.entry(DONE, new DoneItemVisitor(storyService, eventsRegistry)),
                                      Map.entry(IN_PROGRESS, new InProgressItemVisitor(storyService)));
    }

    public void processFor(ToDoItem toDoItem) {
        toDoItem.accept(visitors.get(toDoItem.getStatus()));
    }
}
