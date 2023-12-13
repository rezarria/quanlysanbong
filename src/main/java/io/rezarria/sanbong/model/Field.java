package io.rezarria.sanbong.model;

import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NamedEntityGraph(name = "field-entity-graph", attributeNodes = {
        @NamedAttributeNode("details"),
        @NamedAttributeNode("detail"),
        @NamedAttributeNode("prices"),
        @NamedAttributeNode("price"),
        @NamedAttributeNode("usedHistories")
})
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Field")
public class Field extends Product {
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FieldDetail> details;
    @OneToOne
    private FieldDetail detail;
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonUnwrapped
    private Set<FieldUseHistory> usedHistories;
}
