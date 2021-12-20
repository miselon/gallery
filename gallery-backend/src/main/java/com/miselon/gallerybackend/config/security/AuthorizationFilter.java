package com.miselon.gallerybackend.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.miselon.gallerybackend.config.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired private Properties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Filter secured endpoint
        if(request.getServletPath().equals("/api/files/upload") || request.getServletPath().equals("/api/files/delete")) {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            // Validate auth header
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                // Extract token from the header
                String token = authHeader.split(" ")[1];
                // TODO secret key
                JWTVerifier verifier = JWT.require(Algorithm.HMAC256(properties.getSignSecret())).build();
                DecodedJWT decodedToken = verifier.verify(token);
                // Extract data from the token
                String username = decodedToken.getSubject();
                List<String> roles = decodedToken.getClaim("roles").asList(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r)));
                // Create and store user's auth token
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                // Continue filtering
                filterChain.doFilter(request, response);
                return;
            }
        }
        // Continue filtering
        filterChain.doFilter(request, response);
    }

}
