package io.rezarria.sanbong.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class AuditWith<T> {
    // @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY, orphanRemoval =
    // false)
    // private List<T> creates;
    // @OneToMany(mappedBy = "lastModifiedBy", fetch = FetchType.LAZY, orphanRemoval
    // = false)
    // private List<T> modifies;
}
