package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.ToDoItemReleasedEvent;
import com.smalaca.taskamanager.model.entities.*;
import com.smalaca.taskamanager.model.interfaces.ToDoItemVisitor;
import com.smalaca.taskamanager.registry.EventsRegistry;

class ReleasedItemVisitor implements ToDoItemVisitor {

	private final EventsRegistry eventsRegistry;

	ReleasedItemVisitor(final EventsRegistry eventsRegistry) {
		this.eventsRegistry = eventsRegistry;
	}

	@Override
	public void visit(final Epic epic) {
		ToDoItemReleasedEvent event = new ToDoItemReleasedEvent();
		event.setToDoItemId(epic.getId());
		eventsRegistry.publish(event);
	}

	@Override
	public void visit(final Story story) {
		ToDoItemReleasedEvent event = new ToDoItemReleasedEvent();
		event.setToDoItemId(story.getId());
		eventsRegistry.publish(event);
	}

	@Override
	public void visit(final Task task) {
		ToDoItemReleasedEvent event = new ToDoItemReleasedEvent();
		event.setToDoItemId(task.getId());
		eventsRegistry.publish(event);
	}
}
