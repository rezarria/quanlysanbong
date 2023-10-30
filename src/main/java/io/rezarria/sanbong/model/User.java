package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import io.rezarria.sanbong.converter.JsonNodeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;


@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    protected String name;
    protected String avatar;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date dob;

    @OneToOne(fetch = FetchType.LAZY, cascade = {}, orphanRemoval = false, optional = true)
    @JsonIgnoreProperties("user")
    protected Account account;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonNodeConverter.class)
    protected JsonNode data;
}
