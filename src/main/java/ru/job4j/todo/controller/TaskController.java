package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;

    private final CategoryService categoryService;

    public TaskController(TaskService taskService, PriorityService priorityService, CategoryService categoryService) {
        this.taskService = taskService;
        this.priorityService = priorityService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/list", ""})
    public String getTaskList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(Model model, @ModelAttribute Task task, HttpSession session, @RequestParam("categoryIds") List<Integer> categoryIds) {
        User user = (User) session.getAttribute("user");
        task.setUser(user);
        Set<Category> categories = categoryService.findByIds(categoryIds);
        task.setCategory(categories);
        taskService.save(task);
        return "redirect:/tasks/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model, @RequestParam("categoryIds") List<Integer> categoryIds) {
        Set<Category> categories = categoryService.findByIds(categoryIds);
        task.setCategory(categories);
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @PostMapping("/markFinished")
    public String markFinished(@ModelAttribute Task task, Model model)  {
        var isDone = taskService.markDone(task);
        if (!isDone) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/filter/{status}")
    public String filterTask(@PathVariable boolean status, Model model) {
        model.addAttribute("tasks", taskService.findByStatus(status));
        return "tasks/list";
    }
}
