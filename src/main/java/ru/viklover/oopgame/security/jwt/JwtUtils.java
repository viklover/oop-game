package ru.viklover.oopgame.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.viklover.oopgame.user.User;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationSeconds;

    public String generateJwtToken(User user) {

        var algorithm = Algorithm.HMAC512(jwtSecret);
        var currentTimestamp = new Date().toInstant();

        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withIssuedAt(currentTimestamp)
                .withExpiresAt(currentTimestamp.plusSeconds(jwtExpirationSeconds))
                .sign(algorithm);
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(JWT.decode(token).getSubject());
    }

    public boolean validateToken(String token) {
        if (token == null) return false;
        try {
            JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(token);
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }
}