package io.rezarria.sanbong.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FieldDetail extends BaseEntity {
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode data;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Field field;
    @OneToMany(mappedBy = "detail", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FieldUseHistory> usedHistories;
}
