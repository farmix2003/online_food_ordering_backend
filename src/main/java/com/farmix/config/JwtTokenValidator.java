package com.farmix.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenValidator extends OncePerRequestFilter {


    private final JwtProvider jwtProvider;
     public JwtTokenValidator(){
         jwtProvider = new JwtProvider();
     }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(JwtContstant.JWT_HEADER);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            if (jwtProvider.isTokenBlackListed(jwt)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                return;
            }
            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtContstant.SECRET_KEY.getBytes());
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String email = claims.get("email", String.class);
                String authorities = claims.get("authorities", String.class);

                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                System.err.println("JWT validation error: " + e.getMessage());
                throw new BadCredentialsException("Invalid JWT...", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}