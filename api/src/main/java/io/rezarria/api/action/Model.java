package io.rezarria.api.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.SneakyThrows;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Model {
    @SneakyThrows
    public static <T, O> O update(UUID id, JsonPatch jsonPatch, ObjectMapper objectMapper, Function<UUID, Optional<T>> getUpdate, Function<UUID, Optional<O>> findById, BiConsumer<T, O> patch, Class<T> type) {
        var currentDTO = getUpdate.apply(id).orElseThrow();
        var node = jsonPatch.apply(objectMapper.convertValue(currentDTO, JsonNode.class));
        var patched = objectMapper.treeToValue(node, type);
        var data = findById.apply(id).orElseThrow();
        patch.accept(patched, data);
        return data;
    }
}
