package io.rezarria.sanbong.model;

import java.time.Instant;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Request extends BaseEntity {
    @Nullable
    @Column(name = "`when`")
    private Instant when;
    @Nullable
    private Instant whenAfter;
    @Nullable
    private Instant whenBefore;
    @Nullable
    private Instant end;
    @Nullable
    private Instant endAfter;
    @Nullable
    private Instant endBeforce;
    @ManyToOne(optional = false)
    private Customer customer;
    @Nullable
    @ManyToOne(optional = true)
    private Field field;
    @Nullable
    @ManyToOne(optional = true)
    private Field responseField;
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private Status status = Status.PENDING;

    public enum Status {
        PENDING, DENY, CANCELED, APPROVED
    }
}
