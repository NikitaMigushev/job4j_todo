package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class HbmUserRepositoryTest {

    @Autowired
    private SessionFactory sessionFactory;

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = new HbmUserRepository(sessionFactory);
    }

    @AfterEach
    public void tearDown() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("John Doe");
        Optional<User> savedUser = userRepository.save(user);
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getId()).isNotNull();
    }

    @Test
    public void testFindById() {
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
    public void testFindByEmailAndPassword() {
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