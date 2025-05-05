package dev.saul.descarbonizacion_transporte.modules.auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.saul.descarbonizacion_transporte.modules.auth.entity.Role;
import dev.saul.descarbonizacion_transporte.modules.auth.entity.RoleEntity;

public interface RoleRepository extends MongoRepository<RoleEntity, String> {
    Optional<RoleEntity> findByName(Role name);
}
