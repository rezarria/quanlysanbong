package io.rezarria.sanbong.dto.post;

import java.util.Set;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StaffPostDTO extends UserPostDTO {
    protected String email;
    protected String username;
    protected String password;
    protected Set<UUID> roles;
}
