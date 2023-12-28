package io.rezarria.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

public interface IService<T extends JpaRepository<OBJ, UUID>, OBJ> {

    T getRepo();

    EntityManager getEntityManager();

    @Transactional(readOnly = true)
    default List<OBJ> getAll() {
        return getRepo().findAll();
    }

    @Transactional(readOnly = true)
    default <P> Stream<P> getAllProjection(Class<P> classType, Class<OBJ> rootClassType) {
        var criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<P> query = criteriaBuilder.createQuery(classType);
        var root = query.from(rootClassType);
        query.select(criteriaBuilder.construct(classType, root));
        return getEntityManager().createQuery(query).getResultStream();
    }

    @Transactional()
    default OBJ create(OBJ entity) {
        return getRepo().save(entity);
    }

    @Transactional(readOnly = true)
    default Iterable<OBJ> createMany(Iterable<OBJ> entity) {
        return getRepo().saveAll(entity);
    }

    @Transactional(readOnly = true)
    default OBJ get(UUID id) {
        return getRepo().getReferenceById(id);
    }

    @Transactional(readOnly = true)
    default List<OBJ> getMany(Collection<UUID> ids) {
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

    default <T> Optional<T> getByIdProjection(UUID id, Class<T> type) {
        return null;
    }

    @Transactional
    default OBJ update(OBJ entity) {
        return getEntityManager().merge(entity);
    }

}