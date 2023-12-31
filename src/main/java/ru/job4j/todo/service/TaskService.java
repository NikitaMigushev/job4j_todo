package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {
    Optional<Task> save(Task task);
    boolean update(Task task);
    Optional<Task> findById(int id);
    Collection<Task> findAll();
    Collection<Task> findByStatus(boolean status);
    boolean deleteById(int id);
    boolean markDone(Task task);
}
