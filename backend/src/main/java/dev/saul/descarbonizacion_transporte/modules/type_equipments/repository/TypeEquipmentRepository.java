package dev.saul.descarbonizacion_transporte.modules.type_equipments.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeEquipmentEntity;

@Repository
public interface TypeEquipmentRepository extends MongoRepository<TypeEquipmentEntity, String> {
    @Query("{isActive: true}")
    List<TypeEquipmentEntity> findAllActive();

    @Query("{isActive: true}")
    Optional<TypeEquipmentEntity> findByIdActive(String id);

    @Query("{name: ?0, isActive: true}")
    Optional<TypeEquipmentEntity> findByName(String name);

    Boolean existsByName(String name);
}
