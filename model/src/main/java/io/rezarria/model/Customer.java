package io.rezarria.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
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
    @OneToMany(mappedBy = "customer")
    @Fetch(FetchMode.SELECT)
    @Builder.Default
    private Set<FieldHistory> fieldHistories = new LinkedHashSet<>();
}
