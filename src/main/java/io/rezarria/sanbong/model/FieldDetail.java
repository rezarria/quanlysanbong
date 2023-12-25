package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FieldDetail extends BaseEntity {
    @Transient
    private JsonNode data;
    @ManyToOne
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Field field;
}
