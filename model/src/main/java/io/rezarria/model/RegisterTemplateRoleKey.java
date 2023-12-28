package io.rezarria.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class RegisterTemplateRoleKey implements Serializable {
    UUID registerTemplateId;
    UUID roleId;
}
