package io.rezarria.sanbong.security;

import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public class AccountIdInfoAuthority implements GrantedAuthority {
    private InfoAuthority info;
    public static final String NAME = "AccountId".toUpperCase();

    public AccountIdInfoAuthority(UUID value) {
        info = new InfoAuthority(NAME, value.toString());
    }

    public AccountIdInfoAuthority(GrantedAuthority authority) {
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
