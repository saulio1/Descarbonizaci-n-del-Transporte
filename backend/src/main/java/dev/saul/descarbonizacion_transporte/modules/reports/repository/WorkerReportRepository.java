package dev.saul.descarbonizacion_transporte.modules.reports.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.saul.descarbonizacion_transporte.modules.workers.entity.WorkerEntity;
import dev.saul.descarbonizacion_transporte.modules.workers.repository.WorkerRepository;

@Repository
public interface WorkerReportRepository extends WorkerRepository {
    List<WorkerEntity> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
}
