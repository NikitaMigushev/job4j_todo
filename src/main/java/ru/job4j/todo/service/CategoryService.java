package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryService {
    Collection<Category> findAll();
    Optional<Category> save(Category category);
    Set<Category> findByIds(List<Integer> ids);
}
