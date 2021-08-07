package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.StoryDoneEvent;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.Task;
import com.smalaca.taskamanager.model.enums.ToDoItemStatus;
import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.CommunicationService;
import com.smalaca.taskamanager.service.ProjectBacklogService;
import com.smalaca.taskamanager.service.SprintBacklogService;
import com.smalaca.taskamanager.service.StoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.DONE;
import static com.smalaca.taskamanager.model.enums.ToDoItemStatus.IN_PROGRESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ToDoItemProcessorImplementationOrientedTest {
    private final StoryService storyService = mock(StoryService.class);
    private final EventsRegistry eventsRegistry = mock(EventsRegistry.class);
    private final ProjectBacklogService projectBacklogService = mock(ProjectBacklogService.class);
    private final CommunicationService communicationService = mock(CommunicationService.class);
    private final SprintBacklogService sprintBacklogService = mock(SprintBacklogService.class);

    private final ToDoItemProcessor processor = new ToDoItemProcessor(
            storyService, eventsRegistry, projectBacklogService, communicationService, sprintBacklogService);

    @Test
    void shouldProcessWhenTaskDoneAndStoryNotDone() {
        Task task = givenTask(DONE);
        Story story = Mockito.mock(Story.class);
        BDDMockito.given(story.getStatus()).willReturn(IN_PROGRESS);
        BDDMockito.given(task.getStory()).willReturn(story);

        processor.processFor(task);

        BDDMockito.then(task).should().getStatus();
        BDDMockito.then(task).should(times(2)).getStory();
        BDDMockito.then(storyService).should().updateProgressOf(story, task);
        verifyNoMoreInteractions(task, storyService, eventsRegistry, projectBacklogService, communicationService, sprintBacklogService);
    }

    @Test
    void shouldProcessWhenTaskDoneAndStoryDone() {
        Task task = givenTask(DONE);
        Story story = Mockito.mock(Story.class);
        BDDMockito.given(story.getStatus()).willReturn(DONE);
        Long storyId = 123L;
        BDDMockito.given(story.getId()).willReturn(storyId);
        BDDMockito.given(task.getStory()).willReturn(story);

        processor.processFor(task);

        BDDMockito.then(task).should().getStatus();
        BDDMockito.then(task).should(times(2)).getStory();
        BDDMockito.then(storyService).should().updateProgressOf(story, task);
        ArgumentCaptor<StoryDoneEvent> captor = ArgumentCaptor.forClass(StoryDoneEvent.class);
        BDDMockito.then(eventsRegistry).should().publish(captor.capture());
        Assertions.assertThat(captor.getValue().getStoryId()).isEqualTo(storyId);
        verifyNoMoreInteractions(task, storyService, eventsRegistry, projectBacklogService, communicationService, sprintBacklogService);
    }

    private Task givenTask(ToDoItemStatus status) {
        Task task = Mockito.mock(Task.class);
        BDDMockito.given(task.getStatus()).willReturn(status);
        return task;
    }

    @Test
    void shouldProcessWhenStoryDone() {

    }

    @Test
    void shouldProcessWhenEpicDone() {

    }
}