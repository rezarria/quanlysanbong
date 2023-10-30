package io.rezarria.sanbong.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @OneToMany(mappedBy = "fieldDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FieldUseHistory> FieldUseHistories;
}
