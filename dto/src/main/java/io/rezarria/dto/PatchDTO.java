package io.rezarria.dto;

import com.github.fge.jsonpatch.JsonPatch;

import java.time.Instant;
import java.util.UUID;

public record PatchDTO(UUID id, JsonPatch patch, Instant time) {
}
