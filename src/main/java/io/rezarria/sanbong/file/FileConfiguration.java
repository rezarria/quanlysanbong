package io.rezarria.sanbong.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("storage")
@Data
public class FileConfiguration {
    private String location;
}
