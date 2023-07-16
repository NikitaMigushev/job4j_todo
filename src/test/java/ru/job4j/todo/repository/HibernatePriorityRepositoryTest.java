package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Priority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class HibernatePriorityRepositoryTest {
    private static Session session;
    private static SessionFactory sf;

    private static PriorityRepository priorityRepository;

    @BeforeAll
    public static void setup() throws Exception {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        sf = configuration.buildSessionFactory(serviceRegistry);
        sf = configuration.buildSessionFactory();
        session = sf.openSession();
        priorityRepository = new HibernatePriorityRepository(new CrudRepository(sf));
    }

    @AfterEach
    public void tearDown() {
        session.beginTransaction();
        session.createQuery("DELETE FROM Priority").executeUpdate();
        session.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        // Prepare test data
        Priority priority1 = new Priority(1, "High", 1);
        Priority priority2 = new Priority(2, "Low", 2);
        priorityRepository.save(priority1);
        priorityRepository.save(priority2);

        // Perform the test
        Collection<Priority> priorities = priorityRepository.findAll();

        // Verify the result
        assertThat(priorities)
                .extracting(Priority::getName)
                .containsExactlyInAnyOrder("High", "Low");
    }
}