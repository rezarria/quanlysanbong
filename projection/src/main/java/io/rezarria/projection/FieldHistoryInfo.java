package io.rezarria.projection;

import java.time.Instant;
import java.util.UUID;

public interface FieldHistoryInfo {
    UUID getId();

    int getUnitSize();

    Instant getFrom();

    Instant getTo();
}