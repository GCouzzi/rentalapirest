package com.gsalles.carrental.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {

    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789";
    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 2;
    public static final long EXPIRE_MINUTES = 0;

    private JwtUtils(){

    }

    public static SecretKey generateKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static Date toExpireDate(Date start){
        LocalDateTime ldt = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expire = ldt.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(expire.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken createToken(String username, String role){
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);
        String token = Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(limit)
                .claim("role", role)
                .signWith(generateKey())
                .compact();
        return new JwtToken(token);
    }

    public static Claims getClaimsFromToken(String token){
        try{
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(token.substring(BEARER.length()))
                    .getPayload();
        } catch(JwtException ex){
            log.error("Token inválido." + ex.getMessage());
        }
        return null;
    }

    public static String getUsernameFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

    public static boolean isTokenValid(String token){
        try{
            Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(token.substring(BEARER.length()));
            return true;
        } catch(JwtException ex){
            log.error("Token inválido." + ex.getMessage());
        }
        return false;
    }
}
