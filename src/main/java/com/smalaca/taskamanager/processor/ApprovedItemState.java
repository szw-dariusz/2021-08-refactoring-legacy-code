package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.StoryApprovedEvent;
import com.smalaca.taskamanager.events.TaskApprovedEvent;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.StoryService;

class ApprovedItemState implements ToDoItemState {

	private final StoryService storyService;
	private final EventsRegistry eventsRegistry;

	ApprovedItemState(final StoryService storyService, final EventsRegistry eventsRegistry) {
		this.storyService = storyService;
		this.eventsRegistry = eventsRegistry;
	}

	@Override
	public void process(final ToDoItem toDoItem) {
		if (toDoItem instanceof Story) {
			Story story = (Story)toDoItem;
			StoryApprovedEvent event = new StoryApprovedEvent();
			event.setStoryId(story.getId());
			eventsRegistry.publish(event);
		} else if (toDoItem instanceof Task) {
			Task task = (Task)toDoItem;

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
