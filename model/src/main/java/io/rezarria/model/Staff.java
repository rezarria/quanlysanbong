package io.rezarria.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Staff")
@SuperBuilder
@NoArgsConstructor
public class Staff extends User {
    @OneToMany(mappedBy = "staff")
    @Fetch(FetchMode.SELECT)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    public List<FieldHistory> fieldHistories = new LinkedList<>();
}
