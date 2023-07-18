package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Repository
@Component
@AllArgsConstructor
public class HibernatePriorityRepository implements PriorityRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<Priority> save(Priority priority) {
        try {
            crudRepository.run(session -> session.save(priority));
            return Optional.of(priority);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Collection<Priority> findAll() {
        try {
            return crudRepository.query(
                    "SELECT p FROM Priority p",
                    Priority.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
