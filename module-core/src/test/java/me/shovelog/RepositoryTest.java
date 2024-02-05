package me.shovelog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public abstract class RepositoryTest {

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    protected <T> T save(T entity) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(entity);
            em.flush();
            tx.commit();
            em.clear();
        } catch (Exception e) {
            tx.rollback();
        }
        return entity;
    }

    protected <T> Iterable<T> saveAll(Iterable<T> entities) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (T entity : entities) {
            try {
                em.persist(entity);
                em.flush();
                tx.commit();
                em.clear();
            } catch (Exception e) {
                tx.rollback();
            }
        }
        return entities;
    }
}
