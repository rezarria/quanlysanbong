package io.rezarria.sanbong.security;

import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

public class InfoAuthority implements GrantedAuthority {
    @Getter
    private String key;

    @Getter
    private String value;

    public InfoAuthority(String key, String value) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");
        this.key = key;
        this.value = value;
    }

    public InfoAuthority(String value) {
        var pos = value.indexOf("__[");
        if (pos < 0)
            throw new IllegalArgumentException("Invalid value format");

        key = value.substring(0, pos);
        this.value = value.substring(pos + 3, value.length() - 1);
    }

    @Override
    public String getAuthority() {
        return key.toUpperCase() + "__[" + value + "]";
    }

}
