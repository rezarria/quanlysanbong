package io.rezarria.model;

import java.time.Instant;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Account createdBy;
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Account lastModifiedBy;
    @CreatedDate
    protected Instant createdDate;
    @LastModifiedDate
    protected Instant lastModifiedDate;
}
