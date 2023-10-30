package io.rezarria.sanbong.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Account createdBy;
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Account lastModifiedBy;
    @CreatedDate
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Instant createdDate;
    @LastModifiedDate
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Instant lastModifiedDate;
}
