package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.interfaces.ToDoItemVisitor;
import com.smalaca.taskamanager.service.StoryService;

class InProgressItemVisitor implements ToDoItemVisitor {

	private final StoryService storyService;

	InProgressItemVisitor(final StoryService storyService) {
		this.storyService = storyService;
	}

	@Override
	public void visit(final Task task) {
		storyService.updateProgressOf(task.getStory(), task);
	}
}
