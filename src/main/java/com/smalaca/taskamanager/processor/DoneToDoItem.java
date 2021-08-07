package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.StoryDoneEvent;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.StoryService;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.DONE;

class DoneToDoItem implements ToDoItemState {
    private final StoryService storyService;
    private final EventsRegistry eventsRegistry;

    DoneToDoItem(StoryService storyService, EventsRegistry eventsRegistry) {
        this.storyService = storyService;
        this.eventsRegistry = eventsRegistry;
    }

    @Override
    public void process(ToDoItem toDoItem) {
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
}
