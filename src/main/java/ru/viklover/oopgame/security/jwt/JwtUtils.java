package ru.viklover.oopgame.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import lombok.AllArgsConstructor;
import lombok.Data;

import ru.viklover.oopgame.user.User;

import java.util.Date;

@Data
@AllArgsConstructor
public class JwtUtils {

    private String jwtSecret;
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