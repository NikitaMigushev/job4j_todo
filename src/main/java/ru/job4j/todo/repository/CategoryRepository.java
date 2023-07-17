package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Collection<Category> findAll();
    Optional<Category> save(Category category);

    List<Category> findByIds(List<Integer> ids);
}
