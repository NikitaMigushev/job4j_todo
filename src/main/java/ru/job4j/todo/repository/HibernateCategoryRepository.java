package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.*;

@Repository
@Component
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<Category> save(Category category) {
        try {
            crudRepository.run(session -> session.save(category));
            return Optional.of(category);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Category> findAll() {
        try {
            return crudRepository.query(
                    "SELECT c FROM Category c",
                    Category.class
            );
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Set<Category> findByIds(List<Integer> ids) {
        return new HashSet<>(crudRepository.query("SELECT c FROM Category c WHERE c.id IN :ids",
                Category.class,
                Map.of("ids", ids)));
    }
}
