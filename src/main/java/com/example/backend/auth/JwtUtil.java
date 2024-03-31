package com.example.backend.auth;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.naming.AuthenticationException;

import org.springframework.stereotype.Component;

import com.example.backend.Models.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
    private final String secret = "secretKey";
    private final long tokenValidity = 24 * 60 * 60 * 1000;
    private final JwtParser jwtParser;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(secret);
    }

    public String createToken(UserModel user, String role) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("name", user.getName());
        claims.put("role", role);
        Date tokenValidity = new Date(System.currentTimeMillis() + this.tokenValidity);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if(token != null) {
                return parseJwtClaims(token);
            }
            return null;
        }
        catch (ExpiredJwtException e) {
            req.setAttribute("expired", e.getMessage());
            throw e;
        }
        catch (Exception e) {
            req.setAttribute("invalid", e.getMessage());
        }
        return null;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(TOKEN_HEADER);
        if(bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            System.out.println("Checking validity");
            return claims.getExpiration().after(new Date());
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getSubject(Claims claims) {
        return claims.getSubject();
    }

    public List<String> getRoles(Claims claims) {
        return (List<String>)claims.get("roles");
    }
}
