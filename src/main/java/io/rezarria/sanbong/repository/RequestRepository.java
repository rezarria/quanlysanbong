package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {

}
