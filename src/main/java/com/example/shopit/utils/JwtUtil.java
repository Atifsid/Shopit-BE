package com.example.shopit.utils;

import com.example.shopit.dto.response.UserResponse;
import com.example.shopit.entity.User;
import com.example.shopit.repository.RepositoryAccessor;
import com.example.shopit.repository.ServicesAccessor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "Enter Your Key";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserResponse userDetailResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("UserAuthDetails", userDetailResponse);
        return generateToken(claims, userDetailResponse);
    }

    public boolean isTokenValid(String token, UserResponse userDetailResponse) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetailResponse.getEmail())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(
            Map<String, Object> extraClaims, UserResponse userDetails) {
        return "Bearer " + Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean validateToken(String token) {
        final String username = extractUsername(token);
        Optional<User> user =
                RepositoryAccessor.getUserRepository()
                        .findByEmailAndIsActiveAndIsDeleted(username, true, false);
        return user.isPresent();
    }

    public UserResponse getUserAuthDetailsFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return ServicesAccessor.getModelMapper()
                .map(claims.get("UserAuthDetails"), UserResponse.class);
    }
}