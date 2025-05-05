package dev.saul.descarbonizacion_transporte.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.saul.descarbonizacion_transporte.modules.users.entity.UserEntity;
import dev.saul.descarbonizacion_transporte.modules.users.repository.UserRepository;

/**
 * Implementación del servicio UserDetailsService para cargar detalles de
 * usuario por nombre de usuario.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository usuarioRepository; // Repositorio de usuarios para interactuar con la base de datos

    /**
     * Carga los detalles del usuario por nombre de usuario.
     *
     * @param username El nombre de usuario del usuario.
     * @return UserDetails que contiene la información del usuario.
     * @throws UsernameNotFoundException Si el usuario no es encontrado.
     */
    @Override
    @Transactional // Asegura que el método sea transaccional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Intenta encontrar el usuario por nombre de usuario
        UserEntity user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con el nombre de usuario: " + username));

        // Retorna la implementación de UserDetails para el usuario encontrado
        return UserDetailsImpl.build(user);
    }
}