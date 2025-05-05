package dev.saul.descarbonizacion_transporte.modules.workers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.saul.descarbonizacion_transporte.modules.workers.entity.WorkerEntity;

@Repository
@Primary
public interface WorkerRepository extends MongoRepository<WorkerEntity, String> {
    @Query("{isActive: true}")
    List<WorkerEntity> findAllActive();

    @Query("{isActive: true}")
    Optional<WorkerEntity> findByIdActive(String id);

    @Query("{email: ?0, isActive: true}")
    Optional<WorkerEntity> findByEmail(String email);

    Boolean existsByEmail(String email);
}
