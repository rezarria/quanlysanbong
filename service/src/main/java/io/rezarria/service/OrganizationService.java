package io.rezarria.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.rezarria.model.Field;
import io.rezarria.model.Organization;
import io.rezarria.repository.FieldRepository;
import io.rezarria.repository.OrganizationRepository;
import io.rezarria.security.component.Auth;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationService extends IService<OrganizationRepository, Organization> {

    @Lazy
    private final OrganizationRepository repository;
    @Lazy
    private final FieldRepository fieldRepository;
    @Lazy
    private final EntityManager entityManager;

    @Override
    public OrganizationRepository getRepo() {
        return repository;
    }

    public <T> Optional<T> findByIdProjection(UUID id, Class<T> type) {
        return repository.findByIdProjection(id, type);
    }

    public Optional<Organization> findByAccountId(UUID id) {
        return repository.findByAccounts_Id(id);
    }

    public <T> Page<T> findByIds(Collection<UUID> ids, Pageable page, Class<T> type) {
        return repository.findByIdIn(ids, page, type);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return repository.findByIdProjection(id, type);
    }

    public <A> Stream<A> getAll(Class<A> type) {
        return repository.getStream(type);
    }

    public <A> Page<A> getPage(Pageable page, Class<A> type) {
        var auth = new Auth();
        if (auth.isLogin()) {
            if (auth.hasRole("SUPER_ADMIN"))
                return repository.getPage(page, type);
            else
                return repository.getPageByAccountId(auth.getAccountId(), page, type);
        }
        throw new RuntimeException();
    }

    public Optional<OrganizationDetailInfo> getDetailById(UUID id) {
        return repository.findById(id).map(data -> {
            var fields = fieldRepository.getStreamByOrganizationId(id).map(FieldDetailInfo::create).toList();
            return OrganizationDetailInfo.create(data, fields);
        });
    }

    public static record OrganizationDetailInfo(
                                                UUID id,
                                                String name,
                                                String address,
                                                String email,
                                                String phone,
                                                String image,
                                                List<FieldDetailInfo> fields
    ) {
        public static OrganizationDetailInfo create(Organization organization, List<FieldDetailInfo> fields) {
            return new OrganizationDetailInfo(organization.getId(), organization.getName(), organization.getAddress(), organization.getEmail(), organization.getPhone(), organization.getImage(), fields);
        }
    }

    public static record FieldDetailInfo(
                                         UUID id,
                                         String name,
                                         String description,
                                         List<String> images
    ) {
        public static FieldDetailInfo create(Field field) {
            return new FieldDetailInfo(field.getId(), field.getName(), field.getDescription(), field.getImages().stream().map(i -> i.getPath()).toList());
        }
    }

}
