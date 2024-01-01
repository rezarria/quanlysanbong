package io.rezarria.repository;

import io.rezarria.repository.interfaces.CustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
        this.entityManager = entityManager;
    }

    public CustomRepositoryImpl(Class<O> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();

        Root<?> root = criteriaQuery.from(domainClass);

        var select = criteriaQuery.multiselect(root.getModel().getDeclaredSingularAttributes().stream()
                .map(attr -> root.get(attr.getName())).toArray(Selection[]::new));

        var query = entityManager.createQuery(select);

        query.setFirstResult((int) page.getOffset());
        query.setMaxResults(page.getPageSize());

        var resultList = query.getResultList();

        var result = resultList.stream()
                .map(tuple -> {
                    return entityManager.getReference(type, tuple);
                })
                .toList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(domainClass)));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(result, page, totalCount);
    }
}
