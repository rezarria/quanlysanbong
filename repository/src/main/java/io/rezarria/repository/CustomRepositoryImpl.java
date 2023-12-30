package io.rezarria.repository;

import io.rezarria.repository.interfaces.CustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class CustomRepositoryImpl<O, K> extends SimpleJpaRepository<O, K> implements CustomRepository<O, K> {

    private final Class<O> domainClass;
    @PersistenceContext
    private EntityManager entityManager;

    public CustomRepositoryImpl(JpaEntityInformation<O, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
    }

    public CustomRepositoryImpl(Class<O> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean areIdsExist(Iterable<K> ids) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.from(domainClass);
        Root<O> root = criteriaQuery.from(domainClass);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(root.get("id").in(ids));
        Long count = entityManager.createQuery(criteriaQuery).getSingleResult();
        return count.equals(ids.spliterator().getExactSizeIfKnown());
    }

    @Override
    public <T> Page<T> getPage(Pageable page, Class<T> type) {
        var query = entityManager.getCriteriaBuilder().createQuery(domainClass);
        var root = query.from(domainClass);
        query.select(root);
        entityManager.createQuery(query);
        return null;
    }

}
