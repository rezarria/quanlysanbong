package io.rezarria.dto.update;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;
import java.util.UUID;

public record UserUpdateDTO(UUID id, String name, String avatar, Date dob, JsonNode data) {
}
