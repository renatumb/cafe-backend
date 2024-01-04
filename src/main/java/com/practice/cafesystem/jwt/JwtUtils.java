package com.practice.cafesystem.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtils {

    private static final String SECRET_KEY = "498542f879b3c26081899a3751ac0942f92a06e756390f05ce1c8142740c6762";

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims extractedAllClaims = extractAllClaims(token);
        return claimResolver.apply(extractedAllClaims);
    }

    public Claims extractAllClaims(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public String extractUserName(String token) {
        String extractedName = extractClaim(token, Claims::getSubject);
        return extractedName;
    }

    public Date extractExpiration(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration;
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isvalidToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        final boolean b = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        return b;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateToken(String username, String role) {
        Map<String, Object> roles = new HashMap<>();
        roles.put("role", role);
        return createToken(roles, username);
    }
}
