package io.rezarria.sanbong.dto.post;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class StaffPostDTO extends UserPostDTO {
    protected String email;
    protected String username;
    protected String password;
    protected Set<UUID> roles;
}
