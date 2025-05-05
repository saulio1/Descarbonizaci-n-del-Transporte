package dev.saul.descarbonizacion_transporte.modules.auth.services;

import dev.saul.descarbonizacion_transporte.modules.auth.dto.input.ChangePasswordDto;
import dev.saul.descarbonizacion_transporte.modules.auth.dto.input.LoginInputDto;
import dev.saul.descarbonizacion_transporte.modules.auth.dto.output.LoginOutputDto;
import dev.saul.descarbonizacion_transporte.modules.auth.entity.RoleEntity;
import dev.saul.descarbonizacion_transporte.modules.users.entity.UserEntity;
import dev.saul.descarbonizacion_transporte.modules.users.repository.UserRepository;
import dev.saul.descarbonizacion_transporte.security.jwt.JwtUtils;

import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public LoginOutputDto login(LoginInputDto loginInputDto) {
        // Buscar usuario por username o email
        UserEntity user = userRepository.findByUsername(loginInputDto.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByEmail(loginInputDto.getUsernameOrEmail())
                        .orElse(null));

        if (user == null || !passwordEncoder.matches(loginInputDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Autenticar usando Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginInputDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generar token JWT con los claims necesarios
        String jwt = jwtUtils.generateJwtToken(authentication);

        return mapToLoginResponseDto(user, jwt);
    }

    private LoginOutputDto mapToLoginResponseDto(UserEntity user, String jwt) {
        return new LoginOutputDto(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toSet()));
    }

    public void changePassword(ChangePasswordDto changePasswordDto) {
        // Obtener el usuario autenticado
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar contraseña actual
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        // Actualizar la contraseña
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }
}