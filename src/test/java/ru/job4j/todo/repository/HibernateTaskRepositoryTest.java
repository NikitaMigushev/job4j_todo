package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HibernateTaskRepositoryTest {
    private static Session session;
    private static SessionFactory sf;

    private static TaskRepository taskRepository;
    private static PriorityRepository priorityRepository;
    private static UserRepository userRepository;

    private static CategoryRepository categoryRepository;

    private static User user = new User(1, "user", "user@user.ru", "123", LocalDateTime.now());
    private static List<Category> categories;

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
        taskRepository = new HibernateTaskRepository(new CrudRepository(sf));
        priorityRepository = new HibernatePriorityRepository(new CrudRepository(sf));
        userRepository = new HibernateUserRepository(new CrudRepository(sf));
        categoryRepository = new HibernateCategoryRepository(new CrudRepository(sf));

    }

    @BeforeEach
    public void init() {
        Priority priority1 = new Priority(1, "urgent", 1);
        Priority priority2 = new Priority(2, "normal", 2);
        priorityRepository.save(priority1);
        priorityRepository.save(priority2);
        userRepository.save(user);
        Category category1 = new Category(1, "Bug");
        Category category2 = new Category(2, "Feature");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categories = new ArrayList<>(categoryRepository.findAll());
    }

    @AfterEach
    public void tearDown() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Task").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenSaveThenGetSame() {
        var foundTasks0 = taskRepository.findAll();
        Task task = new Task();
        task.setName("Test");
        task.setDescription("Test");
        task.setDeadline(LocalDate.now());
        Optional<Task> savedTask = taskRepository.save(task);
        var foundTasks1 = taskRepository.findAll();
        assertThat(savedTask).isPresent();
        assertThat(savedTask.get().getId()).isNotNull();
    }

    @Test
    public void whenFindByIdThenSuccess() {
        var priority = priorityRepository.findAll();
        Task task = new Task(1, "Task", "task", LocalDateTime.now(), LocalDate.now(), false, user, priority.iterator().next(), categories);
        var savedTask = taskRepository.save(task);
        Optional<Task> foundTask = taskRepository.findById(savedTask.get().getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getName()).isEqualTo(task.getName());
    }

    @Test
    public void whenUpdateThenSuccess() {
        var priority = priorityRepository.findAll();
        Task task1 = new Task(1, "Task1", "task", LocalDateTime.now(), LocalDate.now(), false, user, priority.iterator().next(), categories);
        var savedTask = taskRepository.save(task1);
        Task task2 = new Task(savedTask.get().getId(), "Task2", "task", LocalDateTime.now(), LocalDate.now(), false, user, priority.iterator().next(), categories);
        taskRepository.update(task2);
        Optional<Task> updatedTask = taskRepository.findById(task2.getId());
        assertThat(updatedTask.get().getName()).isEqualTo(task2.getName());
    }

    @Test
    public void whenFindAllThenSuccess() {
        Collection<Task> beforeTest = taskRepository.findAll();
        var priority = priorityRepository.findAll();
        Task task1 = new Task(1, "Task1", "task", LocalDateTime.now(), LocalDate.now(), false, user, priority.iterator().next(), categories);
        Task task2 = new Task(2, "Task2", "task", LocalDateTime.now(), LocalDate.now(), false, user, priority.iterator().next(), categories);
        taskRepository.save(task1);
        taskRepository.save(task2);
        Collection<Task> afterTest = taskRepository.findAll();
        assertThat(afterTest).hasSize(2);
    }

    @Test
    public void whenFindByStatusSuccess() {
        var priority = priorityRepository.findAll();
        Task task1 = new Task(1, "Task1", "task", LocalDateTime.now(), LocalDate.now(), false, user, priority.iterator().next(), categories);
        Task task2 = new Task(2, "Task2", "task", LocalDateTime.now(), LocalDate.now(), true, user, priority.iterator().next(), categories);
        taskRepository.save(task1);
        taskRepository.save(task2);
        Collection<Task> doneTasks = taskRepository.findByStatus(true);
        Collection<Task> notDoneTasks = taskRepository.findByStatus(false);
        assertThat(doneTasks).hasSize(1);
        assertThat(doneTasks.iterator().next().getName()).isEqualTo("Task2");
        assertThat(notDoneTasks.iterator().next().getName()).isEqualTo("Task1");
    }

    @Test
    public void testDeleteById() {
        Task task = new Task();
        task.setName("TestNotDone");
        task.setDescription("Test");
        taskRepository.save(task);
        boolean isDeleted = taskRepository.deleteById(task.getId());
        assertThat(isDeleted).isTrue();
        Optional<Task> deletedTask = taskRepository.findById(task.getId());
        assertThat(deletedTask).isEmpty();
    }

    @Test
    public void whenMarkDoneThenSuccess() {
        var priority = priorityRepository.findAll();
        Task task = new Task(1, "Task1", "task", LocalDateTime.now(), LocalDate.now(), false, user, priority.iterator().next(), categories);
        taskRepository.save(task);
        boolean markDone = taskRepository.markDone(task);
        Optional<Task> updatedTaskOptional = taskRepository.findById(task.getId());
        assertThat(updatedTaskOptional).isPresent();
        assertThat(updatedTaskOptional.get().isDone()).isTrue();
    }
}