package dev.saul.descarbonizacion_transporte.modules.users.seeders;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.saul.descarbonizacion_transporte.modules.auth.entity.Role;
import dev.saul.descarbonizacion_transporte.modules.auth.entity.RoleEntity;
import dev.saul.descarbonizacion_transporte.modules.auth.repository.RoleRepository;
import dev.saul.descarbonizacion_transporte.modules.users.entity.UserEntity;
import dev.saul.descarbonizacion_transporte.modules.users.repository.UserRepository;

@Service
public class UserSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${seedAdminEmail}")
    private String adminEmail;

    @Value("${seedAdminUsername}")
    private String adminUsername;

    @Value("${seedAdminPassword}")
    private String adminPassword;

    @Value("${seedModeratorEmail}")
    private String moderatorEmail;

    @Value("${seedModeratorUsername}")
    private String moderatorUsername;

    @Value("${seedModeratorPassword}")
    private String moderatorPassword;

    @Value("${seedUserEmail}")
    private String userEmail;

    @Value("${seedUserUsername}")
    private String userUsername;

    @Value("${seedUserPassword}")
    private String userPassword;

    @Autowired
    PasswordEncoder encoder;

    public void seedRoles() {
        var roles = Role.values();
        for (Role role : roles) {
            createRoleIfNotExists(role);
        }
    }

    public void seedUsers() {
        RoleEntity roleAdmin = roleRepository.findByName(Role.ROLE_ADMIN).get();
        RoleEntity roleUser = roleRepository.findByName(Role.ROLE_USER).get();
        RoleEntity roleModerator = roleRepository.findByName(Role.ROLE_MODERATOR).get();

        Optional<UserEntity> userAdminOptional = userRepository.findByUsername(adminUsername);
        if (userAdminOptional.isEmpty()) {
            UserEntity userAdmin = new UserEntity();
            userAdmin.setUsername(adminUsername);
            userAdmin.setEmail(adminEmail);
            userAdmin.setPassword(encoder.encode(adminPassword));

            Set<RoleEntity> rolesAdmin = new HashSet<>();
            rolesAdmin.add(roleAdmin);
            userAdmin.setRoles(rolesAdmin);

            userRepository.save(userAdmin);
        }

        Optional<UserEntity> userModeratorOptional = userRepository.findByUsername(moderatorUsername);
        if (userModeratorOptional.isEmpty()) {
            UserEntity userModerator = new UserEntity();
            userModerator.setUsername(moderatorUsername);
            userModerator.setEmail(moderatorEmail);
            userModerator.setPassword(encoder.encode(moderatorPassword));

            Set<RoleEntity> rolesModerator = new HashSet<>();
            rolesModerator.add(roleModerator);
            userModerator.setRoles(rolesModerator);

            userRepository.save(userModerator);
        }

        Optional<UserEntity> userUserOptional = userRepository.findByUsername(userUsername);
        if (userUserOptional.isEmpty()) {
            UserEntity userGeneral = new UserEntity();
            userGeneral.setUsername(userUsername);
            userGeneral.setEmail(userEmail);
            userGeneral.setPassword(encoder.encode(userPassword));

            Set<RoleEntity> rolesUser = new HashSet<>();
            rolesUser.add(roleUser);
            userGeneral.setRoles(rolesUser);

            userRepository.save(userGeneral);
        }
    }

    private void createRoleIfNotExists(Role role) {
        Optional<RoleEntity> roleOptional = roleRepository.findByName(role);
        if (roleOptional.isEmpty()) {
            roleRepository.save(new RoleEntity(role));
        }
    }
}
