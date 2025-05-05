package dev.saul.descarbonizacion_transporte.security.jwt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Implementación personalizada de AuthenticationEntryPoint para manejar
 * accesos no autorizados.
 */
@Component // Indica que esta clase es un componente de Spring
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class); // Logger para registrar errores

    /**
     * Maneja los intentos de acceso no autorizado.
     *
     * @param request       La solicitud HTTP.
     * @param response      La respuesta HTTP.
     * @param authException La excepción que se lanza durante la autenticación.
     * @throws IOException      Si ocurre una excepción de entrada/salida.
     * @throws ServletException Si ocurre una excepción relacionada con el servlet.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // Registra el intento de acceso no autorizado con el mensaje de la excepción
        logger.error("Error no autorizado: {}", authException.getMessage());

        // Envía una respuesta HTTP con un estado 401 No autorizado y un mensaje de error
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: No autorizado");
    }

}