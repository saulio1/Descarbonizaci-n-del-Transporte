package dev.saul.descarbonizacion_transporte.modules.workers.service;

import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.output.WorkerPositionOutputDto;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.entity.WorkerPositionEntity;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.repository.WorkerPositionRepository;
import dev.saul.descarbonizacion_transporte.modules.workers.dto.input.WorkerInputDto;
import dev.saul.descarbonizacion_transporte.modules.workers.dto.input.WorkerPatchDto;
import dev.saul.descarbonizacion_transporte.modules.workers.dto.output.WorkerOutputDto;
import dev.saul.descarbonizacion_transporte.modules.workers.entity.WorkerEntity;
import dev.saul.descarbonizacion_transporte.modules.workers.repository.WorkerRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {
    private final WorkerRepository repository;

    private final WorkerPositionRepository positionRepository;

    public WorkerService(WorkerRepository repository, WorkerPositionRepository posRepository) {
        this.repository = repository;
        this.positionRepository = posRepository;
    }

    public List<WorkerOutputDto> findAll() {
        return repository.findAllActive().stream().map(this::toDto).toList();
    }

    public WorkerOutputDto findById(String id) {
        return toDto(repository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado")));
    }

    public WorkerOutputDto create(WorkerInputDto userDto) {
        WorkerEntity worker = toEntity(userDto);
        worker.setActive(true);
        return toDto(repository.save(worker));
    }

    public WorkerOutputDto patch(String id, WorkerPatchDto patchDto) {
        WorkerEntity worker = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        if (patchDto.getNames() != null) {
            worker.setNames(patchDto.getNames());
        }
        if (patchDto.getEmail() != null) {
            worker.setEmail(patchDto.getEmail());
        }
        if (patchDto.getLastNames() != null) {
            worker.setLastNames(patchDto.getLastNames());
        }

        if (patchDto.getPhoneNumber() != null) {
            worker.setPhoneNumber(patchDto.getPhoneNumber());
        }

        if (patchDto.getPositionId() != null) {
            var pos = positionRepository.findAll().stream().filter(x -> x.getId().equals(patchDto.getPositionId()))
                    .findFirst();
            if (pos.isEmpty()) {
                throw new RuntimeException("La ocupación no existe");
            }
            worker.setPosition(pos.get());
        }

        if (patchDto.getCid() != null) {
            worker.setCid(patchDto.getCid());
        }

        return toDto(repository.save(worker));
    }

    public void deleteLogical(String id) {
        var worker = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        worker.setActive(false);
        repository.save(worker);
    }

    public void deletePhysical(String id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        repository.deleteById(id);
    }

    private WorkerOutputDto toDto(WorkerEntity worker) {
        var pos = new WorkerPositionOutputDto(worker.getPosition().getId(), worker.getPosition().getName());

        return WorkerOutputDto
                .builder()
                .id(worker.getId())
                .email(worker.getEmail())
                .names(worker.getNames())
                .lastNames(worker.getLastNames())
                .cid(worker.getCid())
                .phoneNumber(worker.getPhoneNumber())
                .position(pos)
                .build();
    }

    private WorkerEntity toEntity(WorkerInputDto dto) {
        Optional<WorkerPositionEntity> pos = positionRepository.findAll().stream()
                .filter(x -> x.getId().equals(dto.getPositionId()))
                .findFirst();
        return new WorkerEntity(
                dto.getNames(),
                dto.getLastNames(),
                dto.getCid(),
                dto.getPhoneNumber(),
                dto.getEmail(),
                pos.get());
    }
}
