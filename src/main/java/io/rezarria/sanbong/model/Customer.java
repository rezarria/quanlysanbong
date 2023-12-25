package io.rezarria.sanbong.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@SuperBuilder()
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Customer")
public class Customer extends User {
    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    private Set<FieldHistory> fieldHistories = new LinkedHashSet<>();

}
