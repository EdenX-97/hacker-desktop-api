package com.eden.hackerdesktopapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Util for JWT
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
@Component
public class JWTUtil {
    private static String secret;
    private static int expiration;
    private static String prefix;
    public static String authorization;

    @Autowired
    private Environment env;

    @PostConstruct
    public void JWTUtil() {
        secret = env.getProperty("jwt.secret");
        expiration = Integer.parseInt(env.getProperty("jwt.expiration"));
        prefix = env.getProperty("jwt.prefix");
        authorization = env.getProperty("jwt.authorization");
    }

    /**
     * Generate a new token by email
     *
     * @param email User's email
     * @return {@link String} JWT
     */
    public static String generateToken(String email) {
        // Use calendar to get current time and calculate expiration time
        Calendar calendar = Calendar.getInstance();
        Date nowTime = calendar.getTime();
        calendar.add(Calendar.SECOND, expiration);
        Date expirationTime = calendar.getTime();

        // ClaimMap is actually claims, parse the token can get it
        HashMap<String, Object> claimMap = new HashMap<>();
        claimMap.put("value", email);

        // Decode secret in config to secret key and use it to create jwt token with prefix
        SecretKey signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        String token = Jwts.builder().setClaims(claimMap).setIssuedAt(nowTime)
                .setExpiration(expirationTime).setNotBefore(nowTime).signWith(signKey).compact();
        token = prefix + token;

        return token;
    }

    //// Need exist claims that from parsed token to refresh the token, return a new token
    //public static String refreshToken(Claims claims) {
    //    // Use calendar to get current time and calculate expiration time
    //    Calendar calendar = Calendar.getInstance();
    //    Date nowTime = calendar.getTime();
    //    calendar.add(Calendar.SECOND, expiration);
    //    Date expirationTime = calendar.getTime();
    //
    //    // ClaimMap is actually claims, parse the token can get it
    //    SecretKey signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    //
    //    // Use exist claims to generate token
    //    String token = Jwts.builder().setClaims(claims).setIssuedAt(nowTime)
    //            .setExpiration(expirationTime).setNotBefore(nowTime).signWith(signKey).compact();
    //    token = prefix + token;
    //
    //    return token;
    //}

    /**
     * Parse token to claims
     *
     * @param token JWT
     * @return {@link Claims} The claims
     * @throws RuntimeException
     */
    public static Claims parseToken(String token) throws RuntimeException {
        String noPrefixToken = token.replace(prefix, "");

        Claims claims;

        try {
            claims = Jwts.parserBuilder().setSigningKey(secret).build()
                    .parseClaimsJws(noPrefixToken).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        return claims;
    }

    //public static String getEmail(String token) throws RuntimeException {
    //    return parseToken(token).get("email").toString();
    //}
}
