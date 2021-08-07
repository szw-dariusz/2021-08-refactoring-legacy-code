package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.EpicReadyToPrioritize;
import com.smalaca.taskamanager.model.entities.*;
import com.smalaca.taskamanager.model.interfaces.ToDoItemVisitor;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.*;

class DefinedItemVisitor implements ToDoItemVisitor {

	private final ProjectBacklogService projectBacklogService;
	private final CommunicationService communicationService;
	private final SprintBacklogService sprintBacklogService;
	private final EventsRegistry eventsRegistry;

	DefinedItemVisitor(final ProjectBacklogService projectBacklogService,
					   final CommunicationService communicationService,
					   final SprintBacklogService sprintBacklogService,
					   final EventsRegistry eventsRegistry) {
		this.projectBacklogService = projectBacklogService;
		this.communicationService = communicationService;
		this.sprintBacklogService = sprintBacklogService;
		this.eventsRegistry = eventsRegistry;
	}

	@Override
	public void visit(final Epic epic) {
		projectBacklogService.putOnTop(epic);
		EpicReadyToPrioritize event = new EpicReadyToPrioritize();
		event.setEpicId(epic.getId());
		eventsRegistry.publish(event);
		communicationService.notify(epic, epic.getProject().getProductOwner());
	}

	@Override
	public void visit(final Story story) {
		if (story.getTasks().isEmpty()) {
			projectBacklogService.moveToReadyForDevelopment(story, story.getProject());
		} else {
			if (!story.isAssigned()) {
				communicationService.notifyTeamsAbout(story, story.getProject());
			}
		}
	}

	@Override
	public void visit(final Task task) {
		sprintBacklogService.moveToReadyForDevelopment(task, task.getCurrentSprint());
	}
}
