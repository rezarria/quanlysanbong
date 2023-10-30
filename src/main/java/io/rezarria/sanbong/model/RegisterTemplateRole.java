package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterTemplateRole extends Audit {
    @EmbeddedId
    private RegisterTemplateRoleKey id;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties({"roles", "hibernateLazyInitializer", "handler"})
    private Role role;
    @ManyToOne
    @MapsId("registerTemplateId")
    @JoinColumn(name = "register_template_id")
    @JsonIgnoreProperties({"roles", "hibernateLazyInitializer", "handler"})
    private RegisterTemplate registerTemplate;
}
