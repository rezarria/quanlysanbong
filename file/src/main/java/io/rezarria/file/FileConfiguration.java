package io.rezarria.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
@Data
public class FileConfiguration {
    private String location;
}
