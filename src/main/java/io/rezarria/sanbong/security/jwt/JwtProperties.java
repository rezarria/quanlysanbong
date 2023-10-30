package io.rezarria.sanbong.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("jwt")
@Component
@Data
public class JwtProperties {
    private String secretKey;

    private long validityInMS;

    private long refreshInMS;
}
