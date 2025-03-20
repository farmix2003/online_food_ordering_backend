package com.farmix.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtProvider {


    private final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.getSecretKey().getBytes());
    private final Set<String> blackListTokens = new HashSet<>();
    public String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String role = populateAuthorities(authorities);


        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+8640000))
                .claim("email", auth.getName())
                .claim("authorities", role)
                .signWith(key).compact();
    }

    public String getEmailFromJwt(String token) {
        token = token.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return String.valueOf(claims.get("email"));
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();

        for (GrantedAuthority authority : authorities){
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

    public void addToBlackList(String jwt) {
        blackListTokens.add(jwt);
    }
    public boolean isTokenBlackListed(String jwt){
        return blackListTokens.contains(jwt);
    }
}
