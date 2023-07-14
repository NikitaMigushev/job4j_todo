package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class HibernateTaskRepositoryTest {
    private static SessionFactory sf;
    private static Session session;

    private static TaskRepository taskRepository;

    @BeforeAll
        public static void setup() throws Exception  {
        var properties = new Properties();
        try (var inputStream = HibernateTaskRepositoryTest.class.getClassLoader().getResourceAsStream("application-test.properties")) {
            properties.load(inputStream);
        }
        var driverClass = properties.getProperty("datasource.driverClassName");
        var url = properties.getProperty("datasource.url");
        var userName = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");
        var dialect = properties.getProperty("jpa.database-platform");
        var hbm2ddl = properties.getProperty("jpa.hibernate.ddl-auto");
        var showSql = properties.getProperty("jpa.show-sql");

            Configuration configuration = new Configuration()
                    .setProperty("hibernate.connection.driver_class", driverClass)
                    .setProperty("hibernate.connection.url", url)
                    .setProperty("hibernate.connection.username", userName)
                    .setProperty("hibernate.connection.password", password)
                    .setProperty("hibernate.dialect", dialect)
                    .setProperty("hibernate.hbm2ddl.auto", hbm2ddl)
                    .setProperty("hibernate.show_sql", showSql)
                    .addAnnotatedClass(Task.class)
                    .addAnnotatedClass(User.class);

            sf = configuration.buildSessionFactory();
            session = sf.openSession();
        taskRepository = new HibernateTaskRepository(new CrudRepository(sf));
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
        Task task = new Task();
        task.setName("Test");
        task.setDescription("Test");
        task.setDeadline(LocalDate.now());
        Optional<Task> savedTask = taskRepository.save(task);
        assertThat(savedTask).isPresent();
        assertThat(savedTask.get().getId()).isNotNull();
    }

    @Test
    public void whenFindByIdThenSuccess() {
        Task task = new Task();
        task.setName("Test");
        task.setDescription("Test");
        task.setDeadline(LocalDate.now());
        taskRepository.save(task);
        Optional<Task> foundTask = taskRepository.findById(task.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getName()).isEqualTo(task.getName());
    }

    @Test
    public void whenUpdateThenSuccess() {
        Task task1 = new Task();
        task1.setName("Test");
        task1.setDescription("Test");
        Task task2 = new Task();
        task2.setName("Test2");
        task2.setDescription("Test");
        Optional<Task> savedTask1 = taskRepository.save(task1);
        task2.setId(savedTask1.get().getId());
        taskRepository.update(task2);
        Optional<Task> updatedTask = taskRepository.findById(savedTask1.get().getId());
        assertThat(updatedTask.get().getName()).isEqualTo(task2.getName());
    }

    @Test
    public void whenFindAllThenSuccess() {
        Task task1 = new Task();
        task1.setName("Test");
        task1.setDescription("Test");
        Task task2 = new Task();
        task2.setName("Test");
        task2.setDescription("Test");
        taskRepository.save(task1);
        taskRepository.save(task2);
        Collection<Task> allTasks = taskRepository.findAll();
        assertThat(allTasks).hasSize(2);
    }

    @Test
    public void whenFindByStatusSuccess() {
        Task task1 = new Task();
        task1.setName("TestNotDone");
        task1.setDescription("Test");
        task1.setDone(false);
        Task task2 = new Task();
        task2.setName("TestDone");
        task2.setDescription("Test");
        task2.setDone(true);
        taskRepository.save(task1);
        taskRepository.save(task2);
        Collection<Task> doneTasks = taskRepository.findByStatus(true);
        Collection<Task> notDoneTasks = taskRepository.findByStatus(false);
        assertThat(doneTasks).hasSize(1);
        assertThat(doneTasks.iterator().next().getName()).isEqualTo("TestDone");
        assertThat(notDoneTasks.iterator().next().getName()).isEqualTo("TestNotDone");
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
        Task task = new Task();
        task.setId(1);
        task.setName("TestNotDone");
        task.setDescription("Test");
        taskRepository.save(task);
        boolean markDone = taskRepository.markDone(task);
        Optional<Task> updatedTaskOptional = taskRepository.findById(task.getId());
        assertThat(updatedTaskOptional).isPresent();
        assertThat(updatedTaskOptional.get().isDone()).isTrue();
    }
}