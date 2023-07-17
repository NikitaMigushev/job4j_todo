package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
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
    private PriorityService priorityService;

    @Mock
    private CategoryService categoryService;
    @Mock
    private Model model;
    @InjectMocks
    private TaskController taskController;
    List<Category> category = new ArrayList<>();

    @BeforeEach
    public void initService() {
        taskService = mock(TaskService.class);
        priorityService = mock(PriorityService.class);
        categoryService = mock(CategoryService.class);
        model = mock(Model.class);
        taskController = new TaskController(taskService, priorityService, categoryService);
        category.add(new Category(1, "bug"));
        category.add(new Category(1, "feature"));
    }

    @Test
    public void testGetTaskListPage() {
        List<Task> tasks = new ArrayList<>();
        User user = new User(1, "Test", "test@test.ru", "123", LocalDateTime.now());
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(),
                LocalDate.now(),
                false, user, new Priority(1, "p1", 1), category);
        Task task2 = new Task(2,
                "Task2",
                "Task2 description",
                LocalDateTime.now(),
                LocalDate.now(),
                false, user, new Priority(1, "p1", 1), category);
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
        User user = new User(1, "Test", "test@test.ru", "123", LocalDateTime.now());
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(), LocalDate.now(),
                false, user, new Priority(1, "p1", 1),  category);
        when(taskService.findById(task1.getId())).thenReturn(Optional.of(task1));
        var model = new ConcurrentModel();
        var view = taskController.getById(model, task1.getId());
        var expectedTask = model.getAttribute("task");
        assertThat(expectedTask).isEqualTo(task1);
        assertThat(view).isEqualTo("tasks/one");
    }

    @Test
    public void testUpdateTask() {
        User user = new User(1, "Test", "test@test.ru", "123", LocalDateTime.now());
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(), LocalDate.now(),
                false, user, new Priority(1, "p1", 1),  category);
        Task task2 = new Task(1,
                "Task2",
                "Task2 description",
                LocalDateTime.now(), LocalDate.now(),
                false, user, new Priority(1, "p1", 1),  category);
        when(taskService.update(task2)).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.update(task2, model, List.of(category.iterator().next().getId()));
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void testDeleteTask() {
        User user = new User(1, "Test", "test@test.ru", "123", LocalDateTime.now());
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(), LocalDate.now(),
                false, user, new Priority(1, "p1", 1),  category);
        when(taskService.deleteById(task1.getId())).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.delete(task1.getId(), model);
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void testMarkFinished() {
        User user = new User(1, "Test", "test@test.ru", "123", LocalDateTime.now());
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(), LocalDate.now(),
                false, user, new Priority(1, "p1", 1),  category);
        when(taskService.findById(task1.getId())).thenReturn(Optional.of(task1));
        when(taskService.markDone(task1)).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.markFinished(task1, model);
        assertThat(view).isEqualTo("redirect:/tasks");

    }

    @Test
    public void testFilterTaskList() {
        User user = new User(1, "Test", "test@test.ru", "123", LocalDateTime.now());
        List<Task> newTasks = new ArrayList<>();
        List<Task> doneTasks = new ArrayList<>();
        Task task1 = new Task(1,
                "Task1",
                "Task1 description",
                LocalDateTime.now(),
                LocalDate.now(),
                false, user, new Priority(1, "p1", 1),  category);
        Task task2 = new Task(2,
                "Task2",
                "Task2 description",
                LocalDateTime.now(),
                LocalDate.now(),
                false, user, new Priority(1, "p1", 1),  category);
        newTasks.add(task1);
        doneTasks.add(task2);
        when(taskService.findByStatus(true)).thenReturn(newTasks);
        var model = new ConcurrentModel();
        var view = taskController.filterTask(true, model);
        assertThat(model.getAttribute("tasks")).isEqualTo(newTasks);
        assertThat(view).isEqualTo("tasks/list");
    }
}