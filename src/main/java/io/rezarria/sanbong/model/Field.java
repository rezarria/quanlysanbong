package io.rezarria.sanbong.model;

import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Field extends BaseEntity {
    private String name;
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("field")
    private Set<FieldPicture> pictures;
    private String description;
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FieldPrice> prices;
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private FieldPrice price;
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
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REFRESH)
    private Organization organization;
}
