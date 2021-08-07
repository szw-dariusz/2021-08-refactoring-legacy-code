package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.StoryDoneEvent;
import com.smalaca.taskamanager.model.entities.Epic;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.interfaces.ToDoItemVisitor;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.StoryService;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.DONE;

class DoneToDoItemVisitor implements ToDoItemVisitor {
    private final StoryService storyService;
    private final EventsRegistry eventsRegistry;

    DoneToDoItemVisitor(StoryService storyService, EventsRegistry eventsRegistry) {
        this.storyService = storyService;
        this.eventsRegistry = eventsRegistry;
    }

    @Override
    public void visit(Task task) {
        Story story = task.getStory();
        storyService.updateProgressOf(task.getStory(), task);
        if (DONE.equals(story.getStatus())) {
            StoryDoneEvent event = new StoryDoneEvent();
            event.setStoryId(story.getId());
            eventsRegistry.publish(event);
        }
    }

    @Override
    public void visit(Story story) {
        StoryDoneEvent event = new StoryDoneEvent();
        event.setStoryId(story.getId());
        eventsRegistry.publish(event);
    }

    @Override
    public void visit(Epic epic) {

    }
}