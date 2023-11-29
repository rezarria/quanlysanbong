package io.rezarria.sanbong.dto;

import java.util.UUID;

public record ChangePasswordDTO(UUID id, String oldPassword, String newPassword) {
}
