package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"/taskList", ""})
    public String getTaskList(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/taskList";
    }

    @GetMapping("/createTask")
    public String getCreationPage(Model model) {
        return "tasks/createTask";
    }

    @PostMapping("/createTask")
    public String create(Model model, @ModelAttribute Task task) {
        try {
            taskService.save(task);
            return "redirect:/tasks/taskList";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/viewTask";
    }

    @PostMapping("/updateTask")
    public String update(@ModelAttribute Task task, Model model) {
        try {
            var isUpdated = taskService.update(task);
            if (!isUpdated) {
                model.addAttribute("message", "Задача с указанным идентификатором не найдена");
                return "errors/404";
            }
            return "redirect:/tasks";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/deleteTask/{id}")
    public String delete(@PathVariable int id, Model model) {
        try {
            var isDeleted = taskService.deleteById(id);
            if (!isDeleted) {
                model.addAttribute("message", "Задача с указанным идентификатором не найдена");
                return "errors/404";
            }
            return "redirect:/tasks";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/markFinished/{id}")
    public String markFinished(@PathVariable int id, Model model) {
        try {
            var taskOptional = taskService.findById(id);
            if (taskOptional.isEmpty()) {
                model.addAttribute("message", "Задача с указанным идентификатором не найден");
                return "errors/404";
            }
            taskService.markDone(taskOptional.get());
            return "redirect:/tasks";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/filterTask/{status}")
    public String filterTask(@PathVariable boolean status, Model model) {
        model.addAttribute("tasks", taskService.findByStatus(status));
        return "tasks/taskList";
    }
}
