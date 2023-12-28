package io.rezarria.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
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
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Customer customer;
    @Nullable
    @ManyToOne()
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Field field;
    @Nullable
    @ManyToOne()
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Field responseField;
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private Status status = Status.PENDING;

    public enum Status {
        PENDING, DENY, CANCELED, APPROVED
    }
}
