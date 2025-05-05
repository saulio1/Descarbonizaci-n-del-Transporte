package dev.saul.descarbonizacion_transporte.modules.type_equipments.dto.input;

import java.util.List;

import dev.saul.descarbonizacion_transporte.global.validator.UniqueField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeEquipmentInputDto {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 30, message = "El nombre debe tener como máximo 30 caracteres")
    private String name;

    @Size(max = 50, message = "La descripción debe tener como máximo 50 caracteres")
    private String description;

    @NotEmpty(message = "La lista de campos no puede estar vacia")
    private List<DataFieldInputDto> dataFields;
}
