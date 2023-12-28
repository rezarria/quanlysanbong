package io.rezarria.repository;

import io.rezarria.repository.interfaces.CustomRepository;
import io.rezarria.model.Request;

import java.util.UUID;

public interface RequestRepository extends CustomRepository<Request, UUID> {

}
