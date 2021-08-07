package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.EpicReadyToPrioritize;
import com.smalaca.taskamanager.exception.UnsupportedToDoItemType;
import com.smalaca.taskamanager.model.entities.*;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.*;

class DefinedItemState implements ToDoItemState {

	private final ProjectBacklogService projectBacklogService;
	private final CommunicationService communicationService;
	private final SprintBacklogService sprintBacklogService;
	private final EventsRegistry eventsRegistry;

	DefinedItemState(final ProjectBacklogService projectBacklogService,
					 final CommunicationService communicationService,
					 final SprintBacklogService sprintBacklogService,
					 final EventsRegistry eventsRegistry) {
		this.projectBacklogService = projectBacklogService;
		this.communicationService = communicationService;
		this.sprintBacklogService = sprintBacklogService;
		this.eventsRegistry = eventsRegistry;
	}

	@Override
	public void process(final ToDoItem toDoItem) {
		if (toDoItem instanceof Story) {
			Story story = (Story)toDoItem;
			if (story.getTasks().isEmpty()) {
				projectBacklogService.moveToReadyForDevelopment(story, story.getProject());
			} else {
				if (!story.isAssigned()) {
					communicationService.notifyTeamsAbout(story, story.getProject());
				}
			}
		} else {
			if (toDoItem instanceof Task) {
				Task task = (Task)toDoItem;
				sprintBacklogService.moveToReadyForDevelopment(task, task.getCurrentSprint());
			} else {
				if (toDoItem instanceof Epic) {
					Epic epic = (Epic)toDoItem;
					projectBacklogService.putOnTop(epic);
					EpicReadyToPrioritize event = new EpicReadyToPrioritize();
					event.setEpicId(epic.getId());
					eventsRegistry.publish(event);
					communicationService.notify(toDoItem, toDoItem.getProject().getProductOwner());
				} else {
					throw new UnsupportedToDoItemType();
				}
			}
		}
	}
}
