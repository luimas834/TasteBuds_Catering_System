package org.TBCS.repository;

import java.util.List;

/**
 * Generic CRUD Repository interface following Interface Segregation Principle (ISP)
 * @param <T> The entity type
 */
public interface CrudRepository<T> {

    void save(T entity);

    T findById(String id);

    List<T> findAll();

    void delete(String id);
}