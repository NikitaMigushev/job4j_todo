package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateCategoryRepositoryTest {
    private static Session session;
    private static SessionFactory sf;

    private static CategoryRepository categoryRepository;

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
        categoryRepository = new HibernateCategoryRepository(new CrudRepository(sf));
    }

    @AfterEach
    public void tearDown() {
        session.beginTransaction();
        session.createQuery("DELETE FROM Category").executeUpdate();
        session.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        Category category1 = new Category(1, "Bug");
        Category category2 = new Category(2, "Feature");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        Collection<Category> categories = categoryRepository.findAll();
        assertThat(categories)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Bug", "Feature");
    }

    @Test
    public void testFindByIds() {
        Category category1 = new Category(1, "Bug");
        Category category2 = new Category(2, "Feature");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        Set<Category> categories = categoryRepository.findByIds(List.of(category1.getId(), category2.getId()));
        assertThat(categories)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Bug", "Feature");
    }
}