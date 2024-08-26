package org.example.clientbank.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.clientbank.security.SysUser.SysUser;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {
    private static String secret = "LejjnLZua6SlR7eZXByD2+9M5P+dYxK3IlfA6XgPksuXijiXMAcpulI03o2Vq+PjYENhgTJGXLNm7YS4f1+IMw==";

    public static String generate() {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());

        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
            .setSubject("User1")
            .setExpiration(accessExpiration)
            .signWith(hmacKey)
            .claim("roles", "USER")
            .claim("firstName", "User1")
            .compact();
    }

    public static JwtAuthentication generate(Claims claims, SysUser user) {
        JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setPrincipal(user);
        jwtInfoToken.setAuthenticated(true);
        return jwtInfoToken;
    }

    private static Set<Roles> getRoles(Claims claims) {
        final List<Map<String, String>> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(r -> Roles.valueOf(r.get("roleName")))
                .collect(Collectors.toSet());
    }

    public enum Roles implements GrantedAuthority {

        USER("USER"), ADMIN("ADMIN");

        private final String value;

        Roles(String value) {
            this.value = value;
        }

        @JsonCreator
        public static Roles fromValue(String value) {
            for (Roles role : Roles.values()) {
                if (role.value.equalsIgnoreCase(value)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Unknown role: " + value);
        }

        @Override
        @JsonValue
        public String getAuthority() {
            return value;
        }
    }
}
