package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @Mock
    private Model model;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void initService() {
        taskService = mock(TaskService.class);
        model = mock(Model.class);
        taskController = new TaskController(taskService);
    }

    @Test
    public void testGetTaskListPage() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(),
                LocalDate.now(),
                false);
        Task task2 = new Task(2,
                "Task2",
                "Task2 description",
                LocalDateTime.now(),
                LocalDate.now(),
                false);
        tasks.add(task1);
        tasks.add(task2);
        when(taskService.findAll()).thenReturn(tasks);
        String viewName = taskController.getTaskList(model);
        verify(taskService, times(1)).findAll();
        verify(model, times(1)).addAttribute("tasks", tasks);
        assertEquals("tasks/list", viewName);
    }

    @Test
    public void testGetCreateTaskPage() {
        model = new ConcurrentModel();
        var view = taskController.getCreationPage(model);
        assertThat(view).isEqualTo("tasks/create");
    }

    @Test
    public void testGetViewTaskPage() {
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(), LocalDate.now(),
                false);
        when(taskService.findById(task1.getId())).thenReturn(Optional.of(task1));
        var model = new ConcurrentModel();
        var view = taskController.getById(model, task1.getId());
        var expectedTask = model.getAttribute("task");
        assertThat(expectedTask).isEqualTo(task1);
        assertThat(view).isEqualTo("tasks/one");
    }

    @Test
    public void testUpdateTask() {
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(), LocalDate.now(),
                false);
        Task task2 = new Task(1,
                "Task2",
                "Task2 description",
                LocalDateTime.now(), LocalDate.now(),
                false);
        when(taskService.update(task2)).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.update(task2, model);
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void testDeleteTask() {
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(), LocalDate.now(),
                false);
        when(taskService.deleteById(task1.getId())).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.delete(task1.getId(), model);
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void testMarkFinished() {
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(), LocalDate.now(),
                false);
        when(taskService.findById(task1.getId())).thenReturn(Optional.of(task1));
        when(taskService.markDone(task1)).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.markFinished(task1, model);
        assertThat(view).isEqualTo("redirect:/tasks");

    }

    @Test
    public void testFilterTaskList() {
        List<Task> newTasks = new ArrayList<>();
        List<Task> doneTasks = new ArrayList<>();
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(),
                LocalDate.now(),
                false);
        Task task2 = new Task(2,
                "Task2",
                "Task2 description",
                LocalDateTime.now(),
                LocalDate.now(),
                false);
        newTasks.add(task1);
        doneTasks.add(task2);
        when(taskService.findByStatus(true)).thenReturn(newTasks);
        var model = new ConcurrentModel();
        var view = taskController.filterTask(true, model);
        assertThat(model.getAttribute("tasks")).isEqualTo(newTasks);
        assertThat(view).isEqualTo("tasks/list");
    }
}