package dev.saul.descarbonizacion_transporte.modules.worker_positions.service;

import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.input.WorkerPositionInputDto;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.output.WorkerPositionOutputDto;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.entity.WorkerPositionEntity;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.repository.WorkerPositionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerPositionService {
    private final WorkerPositionRepository repository;

    public WorkerPositionService(WorkerPositionRepository repository) {
        this.repository = repository;
    }

    public List<WorkerPositionOutputDto> findAll() {
        return repository.findAllActive().stream().map(this::toDto).toList();
    }

    public WorkerPositionOutputDto findById(String id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocupación no encontrada")));
    }

    public WorkerPositionOutputDto create(WorkerPositionInputDto dto) {
        WorkerPositionEntity pos = toEntity(dto);
        pos.setActive(true);
        return toDto(repository.save(pos));
    }

    public WorkerPositionOutputDto put(String id, WorkerPositionInputDto dto) {
        WorkerPositionEntity pos = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocupación no encontrada"));
        pos.setName(dto.getName());

        return toDto(repository.save(pos));
    }

    public void deleteLogical(String id) {
        WorkerPositionEntity user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocupación no encontrada"));

        user.setActive(false);
        repository.save(user);
    }

    public void deletePhysical(String id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocupación no encontrada"));

        repository.deleteById(id);
    }

    private WorkerPositionOutputDto toDto(WorkerPositionEntity pos) {
        return WorkerPositionOutputDto
                .builder()
                .id(pos.getId())
                .name(pos.getName())
                .build();
    }

    private WorkerPositionEntity toEntity(WorkerPositionInputDto posDto) {
        return new WorkerPositionEntity(
                posDto.getName());
    }
}
