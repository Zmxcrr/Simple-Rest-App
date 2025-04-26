import interfaces.CatDao;
import entities.Cat;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;


public class CatDaoImpl implements CatDao {
    private final SessionFactory sessionFactory;

    public CatDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Cat save(Cat cat) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(cat);
            transaction.commit();
            return cat;
        }
    }

    @Override
    public void deleteById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Cat cat = session.get(Cat.class, id);
            if (cat != null) {
                session.remove(cat);
            }
            transaction.commit();
        }
    }

    @Override
    public void delete(Cat cat) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(cat);
            transaction.commit();
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Cat").executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public Cat update(Cat cat) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(cat);
            transaction.commit();
            return cat;
        }
    }

    @Override
    public Cat getById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Cat.class, id);
        }
    }

    @Override
    public List<Cat> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Cat", Cat.class).list();
        }
    }
}