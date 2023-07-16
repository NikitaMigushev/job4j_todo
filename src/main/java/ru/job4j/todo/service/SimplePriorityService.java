package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.PriorityRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimplePriorityService implements PriorityService {
    private final PriorityRepository priorityRepository;

    public SimplePriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @Override
    public Optional<Priority> save(Priority priority) {
        return priorityRepository.save(priority);
    }

    @Override
    public Collection<Priority> findAll() {
        return priorityRepository.findAll();
    }
}
