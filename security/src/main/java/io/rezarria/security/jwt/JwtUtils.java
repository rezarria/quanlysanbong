package io.rezarria.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import io.rezarria.AccountIdInfoAuthority;
import io.rezarria.InfoAuthority;
import io.rezarria.UserIdInfoAuthority;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    public static final String AUTHORITIES_KEY = "roles";

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        String secret = Base64.getEncoder()
                .encodeToString(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private void assginClaims(Collection<? extends GrantedAuthority> authorities, JwtBuilder.BuilderClaims claims) {
        var roles = new ArrayList<GrantedAuthority>();
        var others = new ArrayList<InfoAuthority>();
        authorities.forEach(a -> {
            if (a.getAuthority().startsWith("ROLE_"))
                roles.add(a);
            else
                others.add(new InfoAuthority(a.getAuthority()));
        });
        claims.add(AUTHORITIES_KEY,
                roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        others.forEach(a -> claims.add(a.getKey(), a.getValue()));
    }

    public String createToken(Authentication authentication) {

        String username = authentication.getName();
        JwtBuilder builder = Jwts.builder();
        JwtBuilder.BuilderClaims claims = builder.claims();
        assginClaims(authentication.getAuthorities(), claims);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getValidityInMS());
        return builder
                .subject(username)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey, SIG.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        return getAuthentication(claims);
    }

    public Authentication getAuthentication(Claims claims) {
        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);
        Collection<GrantedAuthority> authorities = null == authoritiesClaim ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());
        var accountId = UUID.fromString(claims.get(AccountIdInfoAuthority.NAME, String.class));
        var userId = UUID.fromString(claims.get(UserIdInfoAuthority.NAME, String.class));
        authorities.add(new AccountIdInfoAuthority(accountId));
        authorities.add(new UserIdInfoAuthority(accountId));
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().isSigned(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims decode(String token) {
        return refresh(token, jwtProperties.getValidityInMS() / 1000);
    }

    public Claims refresh(String token, long seconds) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String refreshToken(String refreshToken) {
        try {
            var builder = Jwts.builder();
            var claims = builder.claims();

            Claims payload = Jwts.parser().decryptWith(secretKey).build().parseSignedClaims(refreshToken).getPayload();

            claims.add(AUTHORITIES_KEY, payload.get(AUTHORITIES_KEY, String.class));
            claims.add(AccountIdInfoAuthority.NAME, payload.get(AccountIdInfoAuthority.NAME, String.class));
            claims.add(UserIdInfoAuthority.NAME, payload.get(UserIdInfoAuthority.NAME, String.class));

            Instant now = Instant.now();
            Instant validity = now.plusMillis(jwtProperties.getValidityInMS()).plusSeconds(60L * 60L);

            return builder
                    .subject(payload.getSubject())
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(validity))
                    .signWith(secretKey, SIG.HS256)
                    .compact();

        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid refresh token", e);
        }
    }

    public String createRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        var authorities = authentication.getAuthorities();
        var builder = Jwts.builder();
        var claims = builder.claims();
        assginClaims(authorities, claims);
        var now = Instant.now();
        var validity = now.plusMillis(jwtProperties.getRefreshInMS()).plusSeconds(60L * 60L * 3L);
        return builder
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(validity))
                .signWith(secretKey, SIG.HS256)
                .compact();
    }

}
