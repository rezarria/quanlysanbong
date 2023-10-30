package io.rezarria.sanbong.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRoleKey implements Serializable {
    private UUID accountId;
    private UUID roleId;
}
