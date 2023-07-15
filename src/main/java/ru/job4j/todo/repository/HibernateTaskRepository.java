package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Repository
@Component
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {
    private final CrudRepository crudRepository;


    @Override
    public Optional<Task> save(Task task) {
        try {
            crudRepository.run(session -> session.save(task));
            return Optional.of(task);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Task task) {
        try {
            crudRepository.run(session -> session.merge(task));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        try {
            return crudRepository.optional(
                    "from Task where id = :fId", Task.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Task> findAll() {
        try {
            return crudRepository.query(
                    "from Task", Task.class
            );
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Task> findByStatus(boolean status) {
        try {
            return crudRepository.query("FROM Task WHERE done = :status", Task.class,
                    Map.of("status", status));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run(
                    "delete from Task where id = :fId",
                    Map.of("fId", id)
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean markDone(Task task) {
        try {
            crudRepository.run(
                    "UPDATE Task SET done = true WHERE id = :fId",
                    Map.of("fId", task.getId())
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}