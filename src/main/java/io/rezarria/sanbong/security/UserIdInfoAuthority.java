package io.rezarria.sanbong.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

public class UserIdInfoAuthority implements GrantedAuthority {

    public static final String NAME = "UserId".toUpperCase();
    private final InfoAuthority info;

    public UserIdInfoAuthority(UUID id) {
        info = new InfoAuthority(NAME, id.toString());
    }

    public UserIdInfoAuthority(GrantedAuthority authority) {
        var str = authority.getAuthority();
        info = new InfoAuthority(str);
    }

    public static boolean check(GrantedAuthority authority) {
        var r = authority.getAuthority();
        return r.startsWith(NAME + "__[") && r.endsWith("]");
    }

    public UUID getValue() {
        return UUID.fromString(info.getValue());
    }

    @Override
    public String getAuthority() {
        return info.getAuthority();
    }
}
