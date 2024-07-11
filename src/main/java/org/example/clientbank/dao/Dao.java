package org.example.clientbank.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void save(T obj);

    boolean delete(T obj);

    void deleteAll(List<T> list);

    void saveAll(List<T> list);

    List<T> findAll();

    boolean deleteById(long id);

    Optional<T> getOne(long id);
}
