package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public List<Category> findByIds(List<Integer> ids) {
        return crudRepository.query(
                "SELECT c FROM Category c WHERE c.id IN :ids",
                Category.class,
                Collections.singletonMap("ids", ids)
        );
    }
}
