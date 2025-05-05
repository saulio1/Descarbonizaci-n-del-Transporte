package dev.saul.descarbonizacion_transporte.modules.type_equipments.service;

import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentEntity;
import dev.saul.descarbonizacion_transporte.modules.equipments.repository.EquipmentRepository;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.dto.input.TypeEquipmentInputDto;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.dto.output.TypeEquipmentOutputDto;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.DataField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeEquipmentEntity;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.repository.TypeEquipmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeEquipmentService {
    @Autowired
    private TypeEquipmentRepository repository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    public List<TypeEquipmentOutputDto> findAll() {
        return repository.findAllActive().stream().map(this::toDto).toList();
    }

    public TypeEquipmentOutputDto findById(String id) {
        return toDto(repository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Tipo de equipamiento no encontrado")));
    }

    public TypeEquipmentOutputDto create(TypeEquipmentInputDto dto) {
        TypeEquipmentEntity typeEquipment = toEntity(dto);
        typeEquipment.setActive(true);
        return toDto(repository.save(typeEquipment));
    }

    public TypeEquipmentOutputDto put(String id, TypeEquipmentInputDto dto) {
        TypeEquipmentEntity type = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de equipamiento no encontrado"));

        if (equipmentRepository.existsByTypeEquipment_IdAndIsActiveTrue(type.getId())) {
            throw new RuntimeException("Tipo de equipamiento no editable");
        }

        type.setName(dto.getName());
        type.setDescription(dto.getDescription());
        type.setDataFields(dto.getDataFields().stream()
                .map(x -> DataField.builder().name(x.getName()).description(x.getDescription()).minimum(x.getMinimum())
                        .maximum(x.getMaximum()).minimumDateTime(x.getMinimumDateTime())
                        .maximumDateTime(x.getMaximumDateTime()).options(x.getOptions()).required(x.isRequired())
                        .typeField(TypeField.valueOf(x.getTypeField())).build())
                .toList());

        return toDto(repository.save(type));
    }

    public void deleteLogical(String id) {
        var typeEquipment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de equipamiento no encontrado"));

        typeEquipment.setActive(false);
        repository.save(typeEquipment);
    }

    public void deletePhysical(String id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de equipamiento no encontrado"));

        repository.deleteById(id);
    }

    private TypeEquipmentOutputDto toDto(TypeEquipmentEntity typeEquipment) {
        return TypeEquipmentOutputDto
                .builder()
                .id(typeEquipment.getId())
                .name(typeEquipment.getName())
                .description(typeEquipment.getDescription())
                .dataFields(typeEquipment.getDataFields())
                .isEditable(!equipmentRepository.existsByTypeEquipment_IdAndIsActiveTrue(typeEquipment.getId()))
                .build();
    }

    private TypeEquipmentEntity toEntity(TypeEquipmentInputDto dto) {
        var dataFields = dto.getDataFields().stream()
                .map(x -> DataField.builder().name(x.getName()).description(x.getDescription()).minimum(x.getMinimum())
                        .maximum(x.getMaximum()).minimumDateTime(x.getMinimumDateTime())
                        .maximumDateTime(x.getMaximumDateTime()).required(x.isRequired())
                        .typeField(TypeField.valueOf(x.getTypeField())).build())
                .toList();
        return new TypeEquipmentEntity(dto.getName(), dto.getDescription(), dataFields);
    }
}
