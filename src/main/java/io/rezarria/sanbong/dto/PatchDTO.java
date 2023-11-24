package io.rezarria.sanbong.dto;

import java.time.Instant;
import java.util.UUID;

import com.github.fge.jsonpatch.JsonPatch;

import lombok.Data;

@Data
public class PatchDTO {
    private UUID id;
    private JsonPatch patch;
    private Instant time;
}
