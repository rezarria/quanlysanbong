package io.rezarria.sanbong.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class Details {
    private final Map<String, String> map;

    public Details() {
        map = new LinkedHashMap<>();
    }

    public static Details from(Claims claims) {
        Details details = new Details();
        claims.entrySet().stream().filter(e -> e.getKey().startsWith("details__")).forEach(e -> details.set(e.getKey().substring(9), (String) e.getValue()));
        return details;
    }

    public void set(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }

    public Stream<Map.Entry<String, String>> stream() {
        return map.entrySet().stream();
    }

    public void addToClaims(JwtBuilder.BuilderClaims claims) {
        map.forEach((k, v) -> claims.add(String.format("details__%s", k), v));
    }

    public UUID getUserId() {
        return UUID.fromString(map.get("userId"));
    }

    public void setUserId(UUID userId) {
        map.put("userId", userId.toString().replaceAll("-", ""));
    }

    public UUID getAccountId() {
        return UUID.fromString(map.get("accountId"));
    }

    public void setAccountId(UUID accountId) {
        map.put("accountId", accountId.toString().replaceAll("-", ""));
    }

}
