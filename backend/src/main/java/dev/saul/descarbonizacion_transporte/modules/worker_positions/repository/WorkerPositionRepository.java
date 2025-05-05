package dev.saul.descarbonizacion_transporte.modules.worker_positions.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.saul.descarbonizacion_transporte.modules.worker_positions.entity.WorkerPositionEntity;

@Repository
public interface WorkerPositionRepository extends MongoRepository<WorkerPositionEntity, String> {
    @Query("{isActive: true}")
    List<WorkerPositionEntity> findAllActive();

    @Query("{isActive: true}")
    Optional<WorkerPositionEntity> findByIdActive(String id);

    @Query("{name: ?0, isActive: true}")
    Optional<WorkerPositionEntity> findByName(String name);

    Boolean existsByName(String name);
}
