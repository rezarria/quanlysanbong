package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@NamedEntityGraph(name = "field-entity-graph", attributeNodes = {
        @NamedAttributeNode("details"),
        @NamedAttributeNode("detail"),
        @NamedAttributeNode("prices"),
        @NamedAttributeNode("price")
})
@EqualsAndHashCode(callSuper = true)
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Field")
public class Field extends Product {
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FieldDetail> details;
    @OneToOne
    private FieldDetail detail;

}
