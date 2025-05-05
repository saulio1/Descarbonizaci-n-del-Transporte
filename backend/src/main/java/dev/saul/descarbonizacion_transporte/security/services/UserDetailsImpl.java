package dev.saul.descarbonizacion_transporte.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.saul.descarbonizacion_transporte.modules.users.entity.UserEntity;

/**
 * Implementación de UserDetails que representa los detalles del usuario en la
 * autenticación.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L; // Identificador de versión para serialización

    private String id; // Identificador único del usuario
    private String username; // Nombre de usuario del usuario
    private String email; // Correo electrónico del usuario

    @JsonIgnore // Previene la serialización del campo de la contraseña
    private String password; // Contraseña del usuario

    private Collection<? extends GrantedAuthority> authorities; // Colección de autoridades (roles) del usuario

    /**
     * Constructor para inicializar UserDetailsImpl.
     *
     * @param id          El identificador único del usuario.
     * @param username    El nombre de usuario del usuario.
     * @param email       El correo electrónico del usuario.
     * @param password    La contraseña del usuario.
     * @param authorities La colección de autoridades del usuario.
     */
    public UserDetailsImpl(String id, String username, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id; // Asigna el ID del usuario
        this.username = username; // Asigna el nombre de usuario
        this.email = email; // Asigna el correo electrónico
        this.password = password; // Asigna la contraseña
        this.authorities = authorities; // Asigna las autoridades
    }

    /**
     * Construye una instancia de UserDetailsImpl a partir de un objeto Usuario.
     *
     * @param user El objeto Usuario.
     * @return Una instancia de UserDetailsImpl.
     */
    public static UserDetailsImpl build(UserEntity user) {
        // Mapea los roles del usuario a GrantedAuthority
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())) // Convierte cada rol a
                                                                                // SimpleGrantedAuthority
                .collect(Collectors.toList()); // Los recoge en una lista

        // Retorna un nuevo objeto UserDetailsImpl
        return new UserDetailsImpl(
                user.getId(), // ID del usuario
                user.getUsername(), // Nombre de usuario
                user.getEmail(), // Correo electrónico
                user.getPassword(), // Contraseña
                authorities); // Autoridades del usuario
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Retorna las autoridades del usuario
    }

    public String getId() {
        return id; // Retorna el ID del usuario
    }

    public String getEmail() {
        return email; // Retorna el correo electrónico
    }

    @Override
    public String getPassword() {
        return password; // Retorna la contraseña
    }

    @Override
    public String getUsername() {
        return username; // Retorna el nombre de usuario
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // La cuenta no está expirada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // La cuenta no está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales no están expirada
    }

    @Override
    public boolean isEnabled() {
        return true; // La cuenta está habilitada
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) // Verifica si el objeto es el mismo
            return true;
        if (o == null || getClass() != o.getClass()) // Verifica si el objeto es nulo o no es de la misma clase
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o; // Convierte a UserDetailsImpl
        return Objects.equals(id, user.id); // Verifica si los IDs son iguales
    }
}