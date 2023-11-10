package io.rezarria.sanbong.service;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IService<T> {

    JpaRepository<T, UUID> getRepo();

    EntityManager getEntityManager();

    default List<T> getAll() {
        return getRepo().findAll();
    }

    default T create(T entity) {
        return getRepo().save(entity);
    }

    default Iterable<T> createMany(Iterable<T> entity) {
        return getRepo().saveAll(entity);
    }

    default T get(UUID id) {
        return getRepo().getReferenceById(id);
    }

    default List<T> getMany(Collection<UUID> ids) {
        return getRepo().findAllById(ids);
    }

    default void remove(UUID id) throws IllegalArgumentException {
        getRepo().deleteById(id);
    }

    default void removeIn(Iterable<UUID> ids) {
        getRepo().deleteAllById(ids);
    }

    default long getSize() {
        return getRepo().count();
    }

    default T update(T entity) {
        return getEntityManager().merge(entity);
    }

}