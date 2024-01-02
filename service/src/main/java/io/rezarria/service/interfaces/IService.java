package io.rezarria.service.interfaces;

import io.rezarria.repository.interfaces.CustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class IService<T extends CustomRepository<O, UUID>, O> {

    protected abstract T getRepo();

    protected abstract EntityManager getEntityManager();

    @Transactional(readOnly = true)
    public List<O> getAll() {
        return getRepo().findAll();
    }

    @Transactional(readOnly = true)
    public <P> Stream<P> getAllProjection(Class<P> classType, Class<O> rootClassType) {
        var criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<P> query = criteriaBuilder.createQuery(classType);
        var root = query.from(rootClassType);
        query.select(criteriaBuilder.construct(classType, root));
        return getEntityManager().createQuery(query).getResultStream();
    }

    @Transactional()
    public O create(O entity) {
        return getRepo().save(entity);
    }

    @Transactional(readOnly = true)
    public Iterable<O> createMany(Iterable<O> entity) {
        return getRepo().saveAll(entity);
    }

    @Transactional(readOnly = true)
    public O get(UUID id) {
        return getRepo().getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public List<O> getMany(Collection<UUID> ids) {
        return getRepo().findAllById(ids);
    }

    public void remove(UUID id) throws IllegalArgumentException {
        getRepo().deleteById(id);
    }

    public void removeIn(Iterable<UUID> ids) {
        getRepo().deleteAllById(ids);
    }

    @Transactional(readOnly = true)
    public long getSize() {
        return getRepo().count();
    }

    @Transactional(readOnly = true)
    public abstract <A> Optional<A> getByIdProjection(UUID id, Class<A> type);

    @Transactional(readOnly = true)
    public <A> Page<A> getPage(Pageable page, Class<A> type) {
        return getRepo().getPage(page, type);
    }

    @Transactional
    public O update(O entity) {
        return getEntityManager().merge(entity);
    }

}