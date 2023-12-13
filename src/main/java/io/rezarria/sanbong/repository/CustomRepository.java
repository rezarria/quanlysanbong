package io.rezarria.sanbong.repository;

import java.util.UUID;

public interface CustomRepository {
    <T> boolean areIdsExist(Iterable<UUID> ids, Class<T> classType);
}
