package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.*;

class DefinedItemState implements ToDoItemState {

	private final DefinedItemVisitor visitor;

	DefinedItemState(final ProjectBacklogService projectBacklogService,
					 final CommunicationService communicationService,
					 final SprintBacklogService sprintBacklogService,
					 final EventsRegistry eventsRegistry) {
		this.visitor = new DefinedItemVisitor(projectBacklogService,
											  communicationService,
											  sprintBacklogService,
											  eventsRegistry);
	}

	@Override
	public void process(final ToDoItem toDoItem) {
		toDoItem.accept(visitor);
	}
}
