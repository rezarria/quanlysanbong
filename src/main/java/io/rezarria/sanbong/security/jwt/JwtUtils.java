package io.rezarria.sanbong.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import io.rezarria.sanbong.security.Details;
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
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import static java.util.stream.Collectors.joining;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private static final String AUTHORITIES_KEY = "roles";

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        String secret = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        JwtBuilder builder = Jwts.builder();
        JwtBuilder.BuilderClaims claims = builder.claims();
        if (!authorities.isEmpty()) {
            claims.add(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        }
        Details details = (Details) authentication.getDetails();
        details.addToClaims(claims);
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
        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities = null == authoritiesClaim ? AuthorityUtils.NO_AUTHORITIES : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());
        User principal = new User(claims.getSubject(), "", authorities);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, token, authorities);
        auth.setDetails(Details.from(claims));
        return auth;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().isSigned(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims refresh(String token) {
        return refresh(token, jwtProperties.getValidityInMS());
    }

    public Claims refresh(String token, long seconds) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .clockSkewSeconds(seconds)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Token quá cũ");
        }
    }

}
