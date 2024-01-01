package io.rezarria.service;

import io.rezarria.model.Staff;
import io.rezarria.repository.StaffRepository;
import io.rezarria.security.component.Auth;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StaffService extends IService<StaffRepository, Staff> {

    private final StaffRepository repository;
    private final EntityManager entityManager;

    @Override
    public StaffRepository getRepo() {
        return repository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public <T> Optional<T> getById(UUID id, Class<T> type) {
        Auth auth = new Auth();
        if (auth.isLogin()) {
            if (auth.hasRole("SUPER_ADMIN"))
                return repository.getByIdProjection(id, type);

            return repository.findByIdAndOrganization_Accounts_Id(id, auth.getAccountId(), type);
        }
        throw new PermissionDeniedDataAccessException("no", new RuntimeException());
    }

    public <T> Page<T> getPage(Pageable page, Class<T> type) {
        Auth auth = new Auth();
        if (auth.isLogin()) {
            if (auth.hasRole("SUPER_ADMIN"))
                return repository.getAllProjection(page, type);

            return repository.findByOrganization_Accounts_Id(auth.getAccountId(), page, type);
        }
        throw new PermissionDeniedDataAccessException("no", new RuntimeException());
    }

    public <T> Stream<T> all(Class<T> type) {
        Auth auth = new Auth();
        if (auth.isLogin()) {
            if (auth.hasRole("SUPER_ADMIN"))
                return repository.getAllStreamProjection(type);
            return repository.getAllStreamProjectionByAccountId(auth.getAccountId(), type);
        }
        throw new PermissionDeniedDataAccessException("no", new RuntimeException());
    }
}
