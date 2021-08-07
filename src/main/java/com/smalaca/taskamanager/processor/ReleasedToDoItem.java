package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.ToDoItemReleasedEvent;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;

class ReleasedToDoItem implements ToDoItemState {
    private final EventsRegistry eventsRegistry;

    ReleasedToDoItem(EventsRegistry eventsRegistry) {
        this.eventsRegistry = eventsRegistry;
    }

    @Override
    public void process(ToDoItem toDoItem) {
        ToDoItemReleasedEvent event = new ToDoItemReleasedEvent();
        event.setToDoItemId(toDoItem.getId());
        eventsRegistry.publish(event);
    }
}
