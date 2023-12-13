package io.rezarria.sanbong.model;

import java.util.Date;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import io.rezarria.sanbong.converter.JsonNodeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = {}, orphanRemoval = false, optional = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Account account;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonNodeConverter.class)
    protected JsonNode data;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Organization organization;
}
