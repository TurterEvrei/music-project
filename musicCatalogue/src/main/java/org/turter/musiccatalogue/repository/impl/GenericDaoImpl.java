package org.turter.musiccatalogue.repository.impl;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.turter.musiccatalogue.repository.GenericDao;

import java.io.Serializable;
import java.util.List;

@Repository
public abstract class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    @Autowired
    protected SessionFactory sessionFactory;

    private final Class<T> clazz;

    protected GenericDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public T findById(ID id) {
        return currentSession().get(clazz, id);
    }

    @Override
    public List<T> findAll() {
        return currentSession()
                .createQuery("from " + clazz.getSimpleName(), clazz)
                .getResultList();
    }

    @Override
    public void save(T entity) {
        currentSession().persist(entity);
    }

    @Override
    public void update(T entity) {
        ID id = (ID) sessionFactory.getPersistenceUnitUtil().getIdentifier(entity);
        if (id == null || currentSession().get(clazz, id) == null) {
            throw new EntityNotFoundException("Entity not found for id: %s. Type: %s"
                    .formatted(id, clazz.getSimpleName()));
        }
        currentSession().merge(entity);
    }

    @Override
    public void delete(ID id) {
        T entity = findById(id);
        if (entity != null) {
            currentSession().remove(entity);
        }
    }
}

