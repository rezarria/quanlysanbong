package io.rezarria.repository.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomRepository<O, K> extends JpaRepository<O, K> {
    boolean areIdsExist(Iterable<K> ids);

    <T> Page<T> getPage(Pageable page, Class<T> type);
}
