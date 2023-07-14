package ru.job4j.todo.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@Component
public class HibernateUserRepository implements UserRepository {
    private final CrudRepository crudRepository;

    public HibernateUserRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<User> save(User user) {
        crudRepository.run(session -> session.save(user));
        return Optional.of(user);
    }

    @Override
    public Optional<User> findById(int id) {
        return crudRepository.optional("FROM User WHERE id = :userId", User.class,
                Map.of("userId", id));
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return crudRepository.optional("FROM User WHERE email = :email AND password = :password",
                User.class, Map.of("email", email, "password", password));
    }

    @Override
    public boolean deleteById(int id) {
        crudRepository.run("DELETE FROM User WHERE id = :userId",
                Map.of("userId", id));
        return true;
    }

    @Override
    public Collection<User> findAll() {
        return crudRepository.query("FROM User", User.class);
    }
}
