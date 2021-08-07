package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.service.StoryService;

class InProgressToDoItem implements ToDoItemState {
    private final StoryService storyService;

    InProgressToDoItem(StoryService storyService) {
        this.storyService = storyService;
    }

    @Override
    public void process(ToDoItem toDoItem) {
        if (toDoItem instanceof Task) {
            Task task = (Task) toDoItem;
            storyService.updateProgressOf(task.getStory(), task);
        }
    }
}
