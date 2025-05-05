package dev.saul.descarbonizacion_transporte.modules.users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.saul.descarbonizacion_transporte.modules.users.entity.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    @Query("{isActive: true}")
    List<UserEntity> findAllActive();

    @Query("{isActive: true}")
    Optional<UserEntity> findByIdActive(String id);

    @Query("{username: ?0, isActive: true}")
    Optional<UserEntity> findByUsername(String username);

    @Query("{email: ?0, isActive: true}")
    Optional<UserEntity> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
