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

import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class HibernateUserRepositoryTest {

    private static SessionFactory sf;
    private static Session session;

    private static UserRepository userRepository;

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
        var show_sql = properties.getProperty("jpa.show-sql");

        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.driver_class", driverClass)
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.username", userName)
                .setProperty("hibernate.connection.password", password)
                .setProperty("hibernate.dialect", dialect)
                .setProperty("hibernate.hbm2ddl.auto", hbm2ddl)
                .setProperty("hibernate.show_sql", show_sql)
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(User.class);

        sf = configuration.buildSessionFactory();
        session = sf.openSession();
        userRepository = new HibernateUserRepository(new CrudRepository(sf));
    }

    @AfterEach
    public void tearDown() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenSaveThenGetSame() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("John Doe");
        Optional<User> savedUser = userRepository.save(user);
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getId()).isNotNull();
    }

    @Test
    public void whenFindByIdThenSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("John Doe");
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenFindByEmailAndPasswordSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("John Doe");
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    public void testDeleteById() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("John Doe");
        userRepository.save(user);
        boolean isDeleted = userRepository.deleteById(user.getId());
        assertThat(isDeleted).isTrue();
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    public void testFindAll() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setFullName("John Doe");
        userRepository.save(user1);
        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setFullName("John Doe");
        userRepository.save(user2);
        Collection<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSize(2);
    }
}