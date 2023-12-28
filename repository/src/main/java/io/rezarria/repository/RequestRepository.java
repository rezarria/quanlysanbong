package io.rezarria.repository;

import io.rezarria.model.Request;
import io.rezarria.repository.interfaces.CustomRepository;

import java.util.UUID;

public interface RequestRepository extends CustomRepository<Request, UUID> {

}
