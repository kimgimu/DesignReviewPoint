package com.kimu.dichamsi.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    public static String getUserEmail(String token, String key){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJwt(token)
                .getBody()
                .get("userEmail",String.class);
    }

    public static boolean isExpired(String token, String key){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJwt(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public static String createToken(String userEmail, String key, Long expireTimeMs){
        Claims claims = Jwts.claims();
        claims.put("userEmail",userEmail);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTimeMs))
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();
    }

}
