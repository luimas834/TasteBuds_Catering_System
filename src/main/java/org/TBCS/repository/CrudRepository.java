package org.TBCS.repository;

import java.util.List;
public interface CrudRepository<T> {

    void save(T entity);

    T findById(String id);

    List<T> findAll();

    void delete(String id);
}