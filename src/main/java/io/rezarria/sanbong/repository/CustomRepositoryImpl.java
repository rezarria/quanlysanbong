package io.rezarria.sanbong.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class CustomRepositoryImpl implements CustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T> boolean areIdsExist(Iterable<UUID> ids, Class<T> classType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.from(classType);
        Root<T> root = criteriaQuery.from(classType);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(root.get("id").in(ids));
        Long count = entityManager.createQuery(criteriaQuery).getSingleResult();
        return count.equals(ids.spliterator().getExactSizeIfKnown());
    }

}
