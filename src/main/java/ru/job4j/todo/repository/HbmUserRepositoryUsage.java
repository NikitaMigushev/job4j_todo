package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.User;

public class HbmUserRepositoryUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            var userRepository = new HbmUserRepository(sf);
            var user = new User();
            user.setFullName("admin");
            user.setEmail("666@mail.ru");
            user.setPassword("admin");
            userRepository.save(user);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
