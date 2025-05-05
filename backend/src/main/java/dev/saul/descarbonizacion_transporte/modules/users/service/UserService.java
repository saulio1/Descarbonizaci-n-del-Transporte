package dev.saul.descarbonizacion_transporte.modules.users.service;

import dev.saul.descarbonizacion_transporte.modules.auth.entity.RoleEntity;
import dev.saul.descarbonizacion_transporte.modules.auth.repository.RoleRepository;
import dev.saul.descarbonizacion_transporte.modules.auth.entity.Role;
import dev.saul.descarbonizacion_transporte.modules.users.dto.input.UserInputDto;
import dev.saul.descarbonizacion_transporte.modules.users.dto.input.UserPatchDto;
import dev.saul.descarbonizacion_transporte.modules.users.dto.output.UserOutputDto;
import dev.saul.descarbonizacion_transporte.modules.users.entity.UserEntity;
import dev.saul.descarbonizacion_transporte.modules.users.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${seedAdminEmail}")
    private String adminEmail;

    @Value("${seedAdminUsername}")
    private String adminUsername;

    public List<UserOutputDto> findAll() {
        return userRepository.findAllActive().stream().map(this::toDto).toList();
    }

    public UserOutputDto findUserById(String id) {
        return toDto(userRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
    }

    public UserOutputDto createUser(UserInputDto userDto) {
        UserEntity user = toEntity(userDto);
        user.setActive(true);
        return toDto(userRepository.save(user));
    }

    public UserOutputDto patchUser(String id, UserPatchDto userPatchDto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (userPatchDto.getUsername() != null) {
            user.setUsername(userPatchDto.getUsername());
        }
        if (userPatchDto.getEmail() != null) {
            user.setEmail(userPatchDto.getEmail());
        }

        if (userPatchDto.getPassword() != null) {
            user.setPassword(userPatchDto.getPassword());
        }

        if (userPatchDto.getRoles() != null) {
            var roles = userPatchDto.getRoles();
            var avaRoles = roleRepository.findAll();
            Set<RoleEntity> r = new HashSet<RoleEntity>();
            avaRoles.forEach(x -> {
                if (roles.contains(x.getName().name()))
                    r.add(x);
            });
            user.setRoles(r);
        }

        return toDto(userRepository.save(user));
    }

    public void deleteLogical(String id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (isLastAdmin(user)) {
            throw new RuntimeException("No se puede eliminar el último administrador del sistema");
        }

        user.setActive(false);
        userRepository.save(user);
    }

    public void deletePhysical(String id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (isLastAdmin(user)) {
            throw new RuntimeException("No se puede eliminar el último administrador del sistema");
        }

        userRepository.deleteById(id);
    }

    public UserOutputDto getMe() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return toDto(user);
    }

    private boolean isLastAdmin(UserEntity user) {
        if (!user.getRoles().stream()
                .map(RoleEntity::getName)
                .anyMatch(Role.ROLE_ADMIN::equals)) {
            return false;
        }

        long adminCount = userRepository.findAllActive().stream()
                .filter(u -> u.getRoles().stream()
                        .map(RoleEntity::getName)
                        .anyMatch(Role.ROLE_ADMIN::equals))
                .count();

        return adminCount <= 1;
    }

    private UserOutputDto toDto(UserEntity user) {
        return UserOutputDto
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(RoleEntity::getName).map(Role::toString)
                        .collect(Collectors.toSet()))
                .build();
    }

    private UserEntity toEntity(UserInputDto userDto) {
        var roles = userDto.getRoles();
        var avaRoles = roleRepository.findAll();
        Set<RoleEntity> r = new HashSet<RoleEntity>();
        avaRoles.forEach(x -> {
            if (roles.contains(x.getName().name()))
                r.add(x);
        });
        return new UserEntity(
                userDto.getUsername(),
                userDto.getEmail(),
                encoder.encode(userDto.getPassword()),
                r);
    }
}