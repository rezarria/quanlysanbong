package io.rezarria.sanbong.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(JsonNode attribute) {
        return attribute.toPrettyString();
    }

    @Override
    @SneakyThrows
    public JsonNode convertToEntityAttribute(String dbData) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(dbData);
    }

}
