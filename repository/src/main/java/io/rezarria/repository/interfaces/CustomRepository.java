package io.rezarria.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomRepository<O, K> extends JpaRepository<O, K> {
    boolean areIdsExist(Iterable<K> ids);
}
