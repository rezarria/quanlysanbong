package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    private String name;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false, mappedBy = "role")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<AccountRole> accounts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false, mappedBy = "role")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<RegisterTemplateRole> registerTemplates = new HashSet<>();
}
