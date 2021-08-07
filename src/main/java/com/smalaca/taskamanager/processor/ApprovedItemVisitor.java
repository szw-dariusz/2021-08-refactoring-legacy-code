package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.StoryApprovedEvent;
import com.smalaca.taskamanager.events.TaskApprovedEvent;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.interfaces.ToDoItemVisitor;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.StoryService;

class ApprovedItemVisitor implements ToDoItemVisitor {

	private final StoryService storyService;
	private final EventsRegistry eventsRegistry;

	ApprovedItemVisitor(final StoryService storyService, final EventsRegistry eventsRegistry) {
		this.storyService = storyService;
		this.eventsRegistry = eventsRegistry;
	}

	@Override
	public void visit(final Story story) {
		StoryApprovedEvent event = new StoryApprovedEvent();
		event.setStoryId(story.getId());
		eventsRegistry.publish(event);
	}

	@Override
	public void visit(final Task task) {
		if (task.isSubtask()) {
			TaskApprovedEvent event = new TaskApprovedEvent();
			event.setTaskId(task.getId());
			eventsRegistry.publish(event);
		} else {
			storyService.attachPartialApprovalFor(task.getStory().getId(), task.getId());
		}
	}
}
