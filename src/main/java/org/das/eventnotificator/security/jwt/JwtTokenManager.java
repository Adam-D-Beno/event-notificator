package org.das.eventnotificator.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtTokenManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenManager.class);
    private final Key secretKey;

    public JwtTokenManager(
            @Value("${jwt.sign_key}") String secretKey
           ) {
        this.secretKey = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }

    public String getLoginFromToken(String jwt) {
        LOGGER.info("Get login from jwt token = {} ", jwt);

        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String jwt) {
        LOGGER.info("Get role from jwt token = {} ", jwt);
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getPayload()
                .get("role", String.class);
    }


    public Long getIdFromToken(String jwt) {
        LOGGER.info("Get id from jwt token = {} ", jwt);
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getPayload()
                .get("id", Long.class);
    }
}
