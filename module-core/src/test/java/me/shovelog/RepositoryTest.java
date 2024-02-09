package me.shovelog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public abstract class RepositoryTest {
    @PersistenceContext
    protected EntityManager em;

    protected <T> T save(T entity) {
        em.persist(entity);
        em.flush();
        em.clear();
        return entity;
    }

    protected <T> Iterable<T> saveAll(Iterable<T> entities) {
        for (T entity : entities) {
            em.persist(entity);
            em.flush();
            em.clear();
        }
        return entities;
    }
}
