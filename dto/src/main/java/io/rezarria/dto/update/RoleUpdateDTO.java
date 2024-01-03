package io.rezarria.dto.update;

import java.util.UUID;

public record RoleUpdateDTO(UUID id, String name, String displayName) {
}
