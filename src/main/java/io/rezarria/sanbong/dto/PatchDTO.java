package io.rezarria.sanbong.dto;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PatchDTO {
    private UUID id;
    private JsonPatch patch;
    private Instant time;
}
