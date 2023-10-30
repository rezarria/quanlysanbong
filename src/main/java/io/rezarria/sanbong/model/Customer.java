package io.rezarria.sanbong.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder()
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {
    @OneToMany(mappedBy = "customer", cascade = {}, fetch = FetchType.LAZY,orphanRemoval = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FieldUseHistory> FieldUseHistories;
}
