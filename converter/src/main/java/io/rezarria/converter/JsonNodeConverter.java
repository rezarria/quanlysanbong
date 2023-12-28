package io.rezarria;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(@Nullable JsonNode attribute) {
        if (attribute == null)
            return "{}";
        return attribute.toPrettyString();
    }

    @Override
    @SneakyThrows
    public JsonNode convertToEntityAttribute(@Nullable String dbData) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(dbData);
    }

}
