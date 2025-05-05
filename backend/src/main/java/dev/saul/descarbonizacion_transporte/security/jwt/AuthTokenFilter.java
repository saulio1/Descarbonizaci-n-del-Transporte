package dev.saul.descarbonizacion_transporte.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.saul.descarbonizacion_transporte.security.services.UserDetailsServiceImpl;

/**
 * Filtro para validar el token JWT y establecer la autenticación del usuario en el contexto de seguridad.
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired // Inyección automática de JwtUtils para manejar operaciones de JWT
    private JwtUtils jwtUtils;

    @Autowired // Inyección automática de UserDetailsServiceImpl para cargar los detalles del usuario
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class); // Logger para registrar errores

    /**
     * Método del filtro para procesar el token JWT y establecer la autenticación.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param filterChain La cadena de filtros para el procesamiento posterior.
     * @throws ServletException Si ocurre una excepción relacionada con el servlet.
     * @throws IOException      Si ocurre una excepción de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Analiza y valida el token JWT de la solicitud
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Obtiene el nombre de usuario del token JWT validado
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Carga los detalles del usuario con el nombre de usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crea un token de autenticación con los detalles del usuario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());

                // Establece detalles adicionales desde la solicitud
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Registra cualquier error que ocurra durante la autenticación
            logger.error("No se puede establecer la autenticación del usuario: {}", e);
        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Analiza el token JWT desde el encabezado Authorization.
     *
     * @param request La solicitud HTTP.
     * @return El token JWT si se encuentra, o null si no se encuentra.
     */
    private String parseJwt(HttpServletRequest request) {
        // Obtiene el encabezado Authorization de la solicitud
        String headerAuth = request.getHeader("Authorization");

        // Verifica si el encabezado es válido y comienza con "Bearer "
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Extrae el token JWT del encabezado
            return headerAuth.substring(7);
        }

        return null; // Devuelve null si no se encuentra un token válido
    }
}