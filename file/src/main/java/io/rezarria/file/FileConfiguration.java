package io.rezarria.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "storage")
@Data
public class FileConfiguration {
    private String location;
}
