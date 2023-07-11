package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
@Component
public class HbmUserRepository implements UserRepository {
    private final SessionFactory sf;

    @Autowired
    public HbmUserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public Optional<User> save(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<User> findById(int id) {
        Session session = sf.openSession();
        try {
            User user = session.get(User.class, id);
            if (user != null) {
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        Session session = sf.openSession();
        try {
            String hql = "FROM User WHERE email = :email AND password = :password";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            User user = query.uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteById(int id) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("DELETE FROM User WHERE id = :userId");
            query.setParameter("userId", id);
            int result = query.executeUpdate();
            session.getTransaction().commit();
            return result > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Collection<User> findAll() {
        Session session = sf.openSession();
        try {
            String hql = "FROM User";
            Query<User> query = session.createQuery(hql, User.class);
            return query.list();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }
}
