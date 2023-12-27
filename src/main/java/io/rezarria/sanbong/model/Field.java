package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private FieldDetail detail;

    @OneToMany(mappedBy = "field")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<FieldUnitSetting> unitSettings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "field")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private List<FieldHistory> histories = new LinkedList<>();

    @OneToOne(orphanRemoval = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "current_unit_setting_id")
    private FieldUnitSetting currentUnitSetting;

}
