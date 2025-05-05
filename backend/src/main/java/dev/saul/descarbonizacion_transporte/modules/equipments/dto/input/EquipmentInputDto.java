package dev.saul.descarbonizacion_transporte.modules.equipments.dto.input;

import java.util.List;

import dev.saul.descarbonizacion_transporte.global.validator.UniqueField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.dto.input.DataFieldInputDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentInputDto {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 30, message = "El nombre debe tener como máximo 30 caracteres")
    private String name;

    @Size(max = 50, message = "La descripción debe tener como máximo 50 caracteres")
    private String description;

    @NotEmpty(message = "La lista de datos no puede estar vacia")
    private List<EquipmentDataFieldInputDto> data;

    private List<DataFieldInputDto> otherDataFields;

    @NotBlank(message = "El tipo de equipamiento es requerido")
    private String typeEquipmentId;

    @NotEmpty(message = "La unidad de medida de la energía consumida es requerida")
    private String usageDataEnergyConsumedMeasurementUnit;

    @NotEmpty(message = "La unidad de medida de la carga o capacidad usada es requerida")
    private String usageDataLoadOrCapacityUsedMeasurementUnit;
}
