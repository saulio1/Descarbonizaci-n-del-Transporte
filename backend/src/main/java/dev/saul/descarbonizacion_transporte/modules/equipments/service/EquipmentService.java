package dev.saul.descarbonizacion_transporte.modules.equipments.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.saul.descarbonizacion_transporte.modules.equipments.dto.input.EquipmentInputDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.dto.input.EquipmentUsageDataFInputDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.dto.input.EquipmentUsageDataInputDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.dto.output.EquipmentOutputDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.dto.output.EquipmentOutputUsageDataDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentDataField;
import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentEntity;
import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentUsageData;
import dev.saul.descarbonizacion_transporte.modules.equipments.repository.EquipmentRepository;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.DataField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeEquipmentEntity;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.repository.TypeEquipmentRepository;

@Service
public class EquipmentService {
        private final EquipmentRepository repository;

        @Autowired
        private TypeEquipmentRepository tpRepository;

        public EquipmentService(EquipmentRepository repository) {
                this.repository = repository;
        }

        public List<EquipmentOutputDto> findAll() {
                return repository.findAllActive().stream().map(this::toDto).toList();
        }

        public EquipmentOutputDto findById(String id) {
                return toDto(repository.findByIdActive(id)
                                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado")));
        }

        public EquipmentOutputUsageDataDto findByIdUsageData(String id) {
                var usageData = repository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado"));
                return EquipmentOutputUsageDataDto.builder().usageData(usageData.getUsageData()).build();
        }

        public EquipmentOutputDto create(EquipmentInputDto dto) {
                TypeEquipmentEntity tp = tpRepository.findById(dto.getTypeEquipmentId())
                                .orElseThrow(() -> new RuntimeException("Tipo de equipamiento no encontrado"));
                EquipmentEntity equipment = toEntity(dto, tp);
                equipment.setActive(true);
                return toDto(repository.save(equipment));
        }

        public void deleteLogical(String id) {
                var equipment = repository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado"));

                equipment.setActive(false);
                repository.save(equipment);
        }

        public void deletePhysical(String id) {
                repository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado"));

                repository.deleteById(id);
        }

        public EquipmentOutputDto put(String id, EquipmentInputDto dto) {
                EquipmentEntity equipment = repository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado"));

                equipment.setName(dto.getName());
                equipment.setDescription(dto.getDescription());

                var dataFields = dto.getOtherDataFields().stream()
                                .map(x -> DataField.builder().name(x.getName()).description(x.getDescription())
                                                .minimum(x.getMinimum())
                                                .maximum(x.getMaximum()).minimumDateTime(x.getMinimumDateTime())
                                                .maximumDateTime(x.getMaximumDateTime()).required(x.isRequired())
                                                .typeField(TypeField.valueOf(x.getTypeField())).build())
                                .toList();
                equipment.setOtherDataFields(dataFields);

                var data = dto.getData().stream()
                                .map(x -> EquipmentDataField.builder().data(x.getData()).fieldName(x.getFieldName())
                                                .build())
                                .toList();
                equipment.setData(data);

                TypeEquipmentEntity tp = tpRepository.findById(dto.getTypeEquipmentId())
                                .orElseThrow(() -> new RuntimeException("Tipo de equipamiento no encontrado"));
                equipment.setTypeEquipment(tp);

                return toDto(repository.save(equipment));
        }

        private LocalDateTime getDateTimeUsageData(EquipmentEntity equipment, String id) {
                var u = equipment.getUsageData().stream().filter(x -> x.getId().equals(id)).findFirst();
                if (u.isPresent())
                        return u.get().getDate();
                return LocalDateTime.now();
        }

        public EquipmentOutputUsageDataDto putUsageData(String id, EquipmentUsageDataInputDto dto) {
                EquipmentEntity equipment = repository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado"));

                var usageData = dto.getUsageData().stream().map(x -> EquipmentUsageData
                                .builder()
                                .id(x.getId())
                                .date(getDateTimeUsageData(equipment, x.getId()))
                                .usageStart(x.getUsageStart())
                                .usageEnd(x.getUsageEnd())
                                .operatingHours(x.getOperatingHours())
                                .energyConsumed(x.getEnergyConsumed())
                                .loadOrCapacityUsed(x.getLoadOrCapacityUsed())
                                .location(x.getLocation())
                                .operator(x.getOperator())
                                .notes(x.getNotes())
                                .build())
                                .toList();

                equipment.setUsageData(usageData);

                return EquipmentOutputUsageDataDto.builder().usageData(repository.save(equipment).getUsageData())
                                .build();
        }

        private EquipmentOutputDto toDto(EquipmentEntity equipment) {

                return EquipmentOutputDto
                                .builder()
                                .id(equipment.getId())
                                .name(equipment.getName())
                                .description(equipment.getDescription())
                                .data(equipment.getData())
                                .otherDataFields(equipment.getOtherDataFields())
                                .typeEquipment(equipment.getTypeEquipment())
                                .usageDataEnergyConsumedMeasurementUnit(
                                                equipment.getUsageDataEnergyConsumedMeasurementUnit())
                                .usageDataLoadOrCapacityUsedMeasurementUnit(
                                                equipment.getUsageDataLoadOrCapacityUsedMeasurementUnit())
                                .build();
        }

        private EquipmentEntity toEntity(EquipmentInputDto dto, TypeEquipmentEntity tp) {
                var dataFields = dto.getOtherDataFields().stream()
                                .map(x -> DataField.builder().name(x.getName()).description(x.getDescription())
                                                .minimum(x.getMinimum())
                                                .maximum(x.getMaximum()).minimumDateTime(x.getMinimumDateTime())
                                                .maximumDateTime(x.getMaximumDateTime()).required(x.isRequired())
                                                .typeField(TypeField.valueOf(x.getTypeField())).build())
                                .toList();
                var data = dto.getData().stream()
                                .map(x -> EquipmentDataField.builder().data(x.getData()).fieldName(x.getFieldName())
                                                .build())
                                .toList();
                return new EquipmentEntity(dto.getName(), dto.getDescription(), data, dataFields,
                                new ArrayList<EquipmentUsageData>(),
                                dto.getUsageDataEnergyConsumedMeasurementUnit(),
                                dto.getUsageDataLoadOrCapacityUsedMeasurementUnit(), tp);
        }
}
