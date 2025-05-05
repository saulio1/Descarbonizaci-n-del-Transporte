package dev.saul.descarbonizacion_transporte.modules.equipments.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentEntity;

@Repository
@Primary
public interface EquipmentRepository extends MongoRepository<EquipmentEntity, String> {
    @Query("{isActive: true}")
    List<EquipmentEntity> findAllActive();

    @Query("{isActive: true}")
    Optional<EquipmentEntity> findByIdActive(String id);

    @Query("{name: ?0, isActive: true}")
    Optional<EquipmentEntity> findByName(String name);

    boolean existsByTypeEquipment_IdAndIsActiveTrue(String typeEquipmentId);

    @Query("{isActive: true}")
    boolean existsByName(String name);
}