package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.StoryDoneEvent;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.enums.ToDoItemStatus;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.DONE;
import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ToDoItemProcessorImplTest {

	private final StoryService storyService = mock(StoryService.class);
	private final EventsRegistry eventsRegistry = mock(EventsRegistry.class);
	private final ProjectBacklogService projectBacklogService = mock(ProjectBacklogService.class);
	private final CommunicationService communicationService = mock(CommunicationService.class);
	private final SprintBacklogService sprintBacklogService = mock(SprintBacklogService.class);
	private final ToDoItemProcessor processor = new ToDoItemProcessor(storyService,
																	  eventsRegistry,
																	  projectBacklogService,
																	  communicationService,
																	  sprintBacklogService);

	@Test
	void shouldProcess_whenTaskDoneAndStoryNotDone() {
		var task = givenTask(DONE);
		var story = mock(Story.class);
		given(story.getStatus()).willReturn(IN_PROGRESS);
		given(task.getStory()).willReturn(story);

		processor.processFor(task);

		then(task).should().getStatus();
		then(task).should(times(2)).getStory();
		then(storyService).should().updateProgressOf(story, task);
		verifyNoMoreInteractions(task,
								 storyService,
								 eventsRegistry,
								 projectBacklogService,
								 communicationService,
								 sprintBacklogService);
	}

	private Task givenTask(final ToDoItemStatus status) {
		Task task = mock(Task.class);
		given(task.getStatus()).willReturn(status);
		return task;
	}

	@Test
	void shouldProcess_whenTaskDoneAndStoryDone() {
		var task = givenTask(DONE);
		var story = mock(Story.class);
		given(story.getStatus()).willReturn(DONE);
		var storyId = 123L;
		given(story.getId()).willReturn(storyId);
		given(task.getStory()).willReturn(story);

		processor.processFor(task);

		then(task).should().getStatus();
		then(task).should(times(2)).getStory();
		then(storyService).should().updateProgressOf(story, task);
		var captor = ArgumentCaptor.forClass(StoryDoneEvent.class);
		then(eventsRegistry).should().publish(captor.capture());
		assertThat(captor.getValue().getStoryId()).isEqualTo(storyId);
		verifyNoMoreInteractions(task,
								 storyService,
								 eventsRegistry,
								 projectBacklogService,
								 communicationService,
								 sprintBacklogService);
	}

	@Test
	void shouldProcess_whenStoryDone() {

	}

	@Test
	void shouldProcess_whenEpicDone() {

	}
}
