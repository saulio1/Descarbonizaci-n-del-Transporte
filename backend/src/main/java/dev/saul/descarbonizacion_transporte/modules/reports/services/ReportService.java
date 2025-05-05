package dev.saul.descarbonizacion_transporte.modules.reports.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentEntity;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.EquipmentByTypeReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.EquipmentUsageReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.LastEquipmentsReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.LastWorkersReportOutDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.TotalEquipmentsReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.TotalWorkersReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.repository.EquipmentReportRepository;
import dev.saul.descarbonizacion_transporte.modules.reports.repository.WorkerReportRepository;
import dev.saul.descarbonizacion_transporte.modules.workers.entity.WorkerEntity;

@Service
public class ReportService {
    @Autowired
    private EquipmentReportRepository equipmentReportRepository;

    @Autowired
    private WorkerReportRepository workerReportRepository;

    public EquipmentUsageReportOutputDto equipmentWithUsage(Optional<List<String>> ids) {
        if (ids.isPresent() && !ids.get().isEmpty())
            return EquipmentUsageReportOutputDto.builder()
                    .data(equipmentReportRepository.findAllActiveByIdIn(ids.get())).build();
        return EquipmentUsageReportOutputDto.builder().data(equipmentReportRepository.findAllActive()).build();
    }

    public List<LastWorkersReportOutDto> lastWorkers() {
        Pageable limit = PageRequest.of(0, 5);
        var data = workerReportRepository.findByIsActiveTrueOrderByCreatedAtDesc(limit);
        return data.stream()
                .map(x -> LastWorkersReportOutDto.builder().id(x.getId()).cid(x.getCid())
                        .position(x.getPosition().getName()).name(x.getNames() + " " + x.getLastNames()).build())
                .toList();
    }

    public List<LastEquipmentsReportOutputDto> lastEquipments() {
        Pageable limit = PageRequest.of(0, 5);
        var data = equipmentReportRepository.findByIsActiveTrueOrderByCreatedAtDesc(limit);
        return data.stream()
                .map(x -> LastEquipmentsReportOutputDto.builder().id(x.getId()).name(x.getName())
                        .description(x.getDescription()).typeEquipment(x.getTypeEquipment().getName()).build())
                .toList();
    }

    public TotalEquipmentsReportOutputDto totalEquipments() {
        List<EquipmentEntity> equipments = equipmentReportRepository.findAllActive();
        int total = equipments.size();

        LocalDateTime endOfYesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MAX);
        int totalYesterday = equipments.stream().filter(x -> x.getCreatedAt().isBefore(endOfYesterday)).toList().size();

        double change = 0;
        if (totalYesterday > 0) {
            change = ((double) (total - totalYesterday) / totalYesterday) * 100;
        } else if (total > 0) {
            change = 100;
        }

        return TotalEquipmentsReportOutputDto.builder().quantity(total).changeByYesterday(change).build();
    }

    public TotalWorkersReportOutputDto totalWorkers() {
        List<WorkerEntity> workers = workerReportRepository.findAllActive();
        int total = workers.size();

        LocalDateTime endOfYesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MAX);
        int totalYesterday = workers.stream().filter(x -> x.getCreatedAt().isBefore(endOfYesterday)).toList().size();

        double change = 0;
        if (totalYesterday > 0) {
            change = ((double) (total - totalYesterday) / totalYesterday) * 100;
        } else if (total > 0) {
            change = 100;
        }

        return TotalWorkersReportOutputDto.builder().quantity(total).changeByYesterday(change).build();
    }

    public List<EquipmentByTypeReportOutputDto> countEquipmentsByType() {
        List<EquipmentEntity> equipments = equipmentReportRepository.findAllActive();

        Map<String, Long> countByType = equipments.stream()
                .filter(e -> e.getTypeEquipment() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getTypeEquipment().getName(),
                        Collectors.counting()));

        return countByType.entrySet().stream()
                .map(entry -> EquipmentByTypeReportOutputDto.builder()
                        .typeEquipment(entry.getKey())
                        .quantity(entry.getValue().intValue())
                        .build())
                .collect(Collectors.toList());
    }
}
