package io.rezarria.service.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.rezarria.repository.interfaces.CustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

public interface IService<T extends CustomRepository<O, UUID>, O> {

    T getRepo();

    EntityManager getEntityManager();

    @Transactional(readOnly = true)
    default List<O> getAll() {
        return getRepo().findAll();
    }

    @Transactional(readOnly = true)
    default <P> Stream<P> getAllProjection(Class<P> classType, Class<O> rootClassType) {
        var criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<P> query = criteriaBuilder.createQuery(classType);
        var root = query.from(rootClassType);
        query.select(criteriaBuilder.construct(classType, root));
        return getEntityManager().createQuery(query).getResultStream();
    }

    @Transactional()
    default O create(O entity) {
        return getRepo().save(entity);
    }

    @Transactional(readOnly = true)
    default Iterable<O> createMany(Iterable<O> entity) {
        return getRepo().saveAll(entity);
    }

    @Transactional(readOnly = true)
    default O get(UUID id) {
        return getRepo().getReferenceById(id);
    }

    @Transactional(readOnly = true)
    default List<O> getMany(Collection<UUID> ids) {
        return getRepo().findAllById(ids);
    }

    default void remove(UUID id) throws IllegalArgumentException {
        getRepo().deleteById(id);
    }

    default void removeIn(Iterable<UUID> ids) {
        getRepo().deleteAllById(ids);
    }

    @Transactional(readOnly = true)
    default long getSize() {
        return getRepo().count();
    }

    @Transactional(readOnly = true)
    default <A> Optional<T> getByIdProjection(UUID id, Class<A> type) {
        return null;
    }

    @Transactional(readOnly = true)
    default <A> Page<A> getPage(Pageable page, Class<A> type) {
        return null;
    }

    @Transactional
    default O update(O entity) {
        return getEntityManager().merge(entity);
    }

}