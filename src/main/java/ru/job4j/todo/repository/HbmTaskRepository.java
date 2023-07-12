package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
@Component
public class HbmTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    @Autowired
    public HbmTaskRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public Optional<Task> save(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            return Optional.of(task);
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        try {
            Task task = session.get(Task.class, id);
            if (task != null) {
                return Optional.of(task);
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
    public Collection<Task> findAll() {
        Session session = sf.openSession();
        try {
            String hql = "FROM Task";
            Query<Task> query = session.createQuery(hql, Task.class);
            return query.list();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Collection<Task> findByStatus(boolean status) {
        Session session = sf.openSession();
        try {
            String hql = "FROM Task WHERE done = :status";
            Query<Task> query = session.createQuery(hql, Task.class);
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
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
            Query<User> query = session.createQuery("DELETE FROM Task WHERE id = :taskId");
            query.setParameter("taskId", id);
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
}