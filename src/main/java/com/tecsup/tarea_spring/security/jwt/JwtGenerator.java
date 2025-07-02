package com.tecsup.tarea_spring.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey; // ¡IMPORTACIÓN CLAVE!
import java.time.Instant;
import java.util.Date;

@Component
public class JwtGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JwtGenerator.class);

    @Value("${app.jwt.secret:tu_clave_secreta_super_segura_y_larga_aqui_cambiala_en_produccion}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}") // Por defecto 24 horas en milisegundos
    private int jwtExpirationMs;

    // Método para obtener la clave de firma JWT
    // ¡CAMBIO AQUÍ! Ahora retorna SecretKey en lugar de Key genérica.
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Método para generar un token JWT
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Instant now = Instant.now();
        Instant expirationInstant = now.plusMillis(jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationInstant))
                .signWith(getSigningKey(), Jwts.SIG.HS512) // Ya no habrá error aquí
                .compact();
    }

    // Método para obtener el nombre de usuario de un token JWT
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey()) // Ya no habrá error aquí
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // Método para validar un token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey()) // Ya no habrá error aquí
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }
        return false;
    }
}