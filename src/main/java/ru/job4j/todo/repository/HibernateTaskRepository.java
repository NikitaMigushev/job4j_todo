package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@Component
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {
    private final CrudRepository crudRepository;


    @Override
    public Optional<Task> save(Task task) {
        crudRepository.run(session -> session.save(task));
        return Optional.of(task);
    }

    @Override
    public boolean update(Task task) {
        crudRepository.run(session -> session.merge(task));
        return true;
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "from Task where id = :fId", Task.class,
                Map.of("fId", id)
        );
    }

    @Override
    public Collection<Task> findAll() {
        return crudRepository.query(
                "from Task", Task.class
        );
    }

    @Override
    public Collection<Task> findByStatus(boolean status) {
        return crudRepository.query("FROM Task WHERE done = :status", Task.class,
                Map.of("status", status));
    }

    @Override
    public boolean deleteById(int id) {
        crudRepository.run(
                "delete from Task where id = :fId",
                Map.of("fId", id)
        );
        return true;
    }

    @Override
    public boolean markDone(Task task) {
        crudRepository.run(session -> {
            task.setDone(true);
            session.update(task);
        });
        return true;
    }
}