package dev.saul.descarbonizacion_transporte.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import dev.saul.descarbonizacion_transporte.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Clase utilitaria para gestionar los JSON Web Tokens (JWT).
 */
@Component // Indica que esta clase es un componente de Spring
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class); // Logger para registrar errores

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT basado en la autenticación proporcionada.
     *
     * @param authentication El objeto de autenticación que contiene los detalles
     *                       del usuario.
     * @return El token JWT generado como una cadena.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setId(userPrincipal.getId()) // ID del usuario
                .setSubject(userPrincipal.getUsername()) // Username
                .claim("email", userPrincipal.getEmail()) // Email
                .claim("roles", userPrincipal.getAuthorities() // Roles
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Expiración
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Crea una clave de firma a partir del secreto del JWT.
     *
     * @return La clave de firma como un objeto Key.
     */
    private Key key() {
        // Decodifica el secreto del JWT y crea una clave de firma
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extrae el nombre de usuario del token JWT proporcionado.
     *
     * @param token El token JWT.
     * @return El nombre de usuario extraído del token.
     */
    public String getUserNameFromJwtToken(String token) {
        // Analiza el token JWT y retorna el sujeto (nombre de usuario)
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Valida el token JWT proporcionado.
     *
     * @param authToken El token JWT a validar.
     * @return True si el token es válido, false si no lo es.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // Analiza el token y verifica su firma
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true; // El token es válido
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage()); // Registra error de token inválido
        } catch (ExpiredJwtException e) {
            logger.error("El token JWT ha expirado: {}", e.getMessage()); // Registra error de token expirado
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage()); // Registra error de token no soportado
        } catch (IllegalArgumentException e) {
            logger.error("La cadena de claims del JWT está vacía: {}", e.getMessage()); // Registra error de claims
                                                                                        // vacíos
        }

        return false; // El token no es válido
    }
}