package dev.saul.descarbonizacion_transporte.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import dev.saul.descarbonizacion_transporte.security.jwt.AuthEntryPointJwt;
import dev.saul.descarbonizacion_transporte.security.jwt.AuthTokenFilter;
import dev.saul.descarbonizacion_transporte.security.services.UserDetailsServiceImpl;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Clase de configuración de seguridad para configurar Spring Security.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService; // Inyecta el servicio de detalles de usuario para la autenticación

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler; // Inyecta el punto de entrada para solicitudes no autorizadas

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*"); // Para permitir cualquier origen (con wildcard)
        configuration.addAllowedHeader("*"); // Permitir cualquier encabezado
        configuration.addAllowedMethod("*"); // Permitir cualquier método (GET, POST, etc.)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Crea un bean para el filtro de token JWT de autenticación.
     *
     * @return Instancia de AuthTokenFilter
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(); // Retorna una nueva instancia de AuthTokenFilter
    }

    /**
     * Crea un bean para el proveedor de autenticación DAO.
     *
     * @return Instancia de DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Crea un nuevo proveedor de
                                                                                  // autenticación

        authProvider.setUserDetailsService(userDetailsService); // Establece el servicio de detalles de usuario
        authProvider.setPasswordEncoder(passwordEncoder()); // Establece el codificador de contraseñas

        return authProvider; // Retorna el proveedor de autenticación configurado
    }

    /**
     * Crea un bean para el gestor de autenticación.
     *
     * @param authConfig Configuración de autenticación
     * @return Instancia de AuthenticationManager
     * @throws Exception si hay un error al obtener el gestor de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Retorna el gestor de autenticación de la configuración
    }

    /**
     * Crea un bean para el codificador de contraseñas.
     *
     * @return Instancia de PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Retorna una nueva instancia de BCryptPasswordEncoder
    }

    /**
     * Configura la cadena de filtros de seguridad para las solicitudes HTTP.
     *
     * @param http Configuración de HttpSecurity
     * @return Instancia de SecurityFilterChain
     * @throws Exception si hay un error al configurar la cadena de filtros de
     *                   seguridad
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configura la protección CSRF, el CORS, el manejo de excepciones, la gestión
        // de
        // sesiones y
        // la autorización
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita la protección CSRF
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // Establece el manejador de solicitudes no autorizadas
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Establece la política de sesión a stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**", // Rutas de Swagger
                                "/v3/api-docs/**", // Documentacion de Swagger
                                "/swagger-resources/**", // Recursos de Swagger
                                "/swagger-ui.html", // Página principal de Swagger
                                "/webjars/**" // Archivos estáticos de Swagger
                        ).permitAll()
                        // Configura la autorización para las solicitudes HTTP
                        .requestMatchers("/api/auth/login").permitAll()
                        // Permite acceso público a los puntos finales de autenticación
                        .requestMatchers("/api/**").authenticated()
                        // Permite acceso público a los puntos finales de prueba
                        .anyRequest().authenticated());
        // Requiere autenticación para cualquier otra solicitud

        http.authenticationProvider(authenticationProvider()); // Establece el proveedor de autenticación

        // Agrega el filtro de token JWT antes del filtro de autenticación de
        // usuario/contraseña
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Construye y retorna la cadena de filtros de seguridad
    }
}