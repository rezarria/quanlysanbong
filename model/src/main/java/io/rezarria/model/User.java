package io.rezarria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import io.rezarria.converter.JsonNodeConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    protected String name;

    protected String avatar;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date dob;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("user")
    @JoinColumn(name = "account_id")
    protected Account account;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonNodeConverter.class)
    protected JsonNode data;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnoreProperties("users")
    private Organization organization;
}
