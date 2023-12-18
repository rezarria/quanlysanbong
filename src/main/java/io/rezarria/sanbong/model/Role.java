package io.rezarria.sanbong.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    private String name;
    private String displayName;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "role_id")
    @Builder.Default
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<AccountRole> accounts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false, mappedBy = "role")
    @Builder.Default
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RegisterTemplateRole> registerTemplates = new HashSet<>();
}
