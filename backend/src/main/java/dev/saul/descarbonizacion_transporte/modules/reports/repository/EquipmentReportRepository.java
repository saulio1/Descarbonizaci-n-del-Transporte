package dev.saul.descarbonizacion_transporte.modules.reports.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentEntity;
import dev.saul.descarbonizacion_transporte.modules.equipments.repository.EquipmentRepository;

@Repository
public interface EquipmentReportRepository extends EquipmentRepository {
    @Query("{'_id': { $in: ?0 }, 'isActive': true}")
    List<EquipmentEntity> findAllActiveByIdIn(List<String> equipmentIds);

    List<EquipmentEntity> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
}
