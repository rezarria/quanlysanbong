package io.rezarria.sanbong.security;

import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public class UserIdInfoAuthority implements GrantedAuthority {

    private InfoAuthority info;
    public static final String NAME = "UserId".toUpperCase();

    public UserIdInfoAuthority(UUID id) {
        info = new InfoAuthority(NAME, id.toString());
    }

    public UUID getValue() {
        return UUID.fromString(info.getValue());
    }

    public UserIdInfoAuthority(GrantedAuthority authority) {
        var str = authority.getAuthority();
        info = new InfoAuthority(str);
    }

    public static boolean check(GrantedAuthority authority) {
        var r = authority.getAuthority();
        return r.startsWith(NAME + "__[") && r.endsWith("]");
    }

    @Override
    public String getAuthority() {
        return info.getAuthority();
    }
}
