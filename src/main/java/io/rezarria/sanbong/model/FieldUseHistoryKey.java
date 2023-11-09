package io.rezarria.sanbong.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.UUID;

@Embeddable
public class FieldUseHistoryKey {
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    UUID fieldId;
    UUID staffId;
    UUID customerId;
    UUID fieldDetailId;
    UUID fieldPriceId;
}
