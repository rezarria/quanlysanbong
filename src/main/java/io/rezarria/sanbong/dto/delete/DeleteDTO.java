package io.rezarria.sanbong.dto.delete;

import java.util.Set;
import java.util.UUID;

public record DeleteDTO(Set<UUID> ids) {
}