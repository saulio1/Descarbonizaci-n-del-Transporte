package dev.saul.descarbonizacion_transporte.modules.equipments.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDataFieldInputDto {
    private String data;

    @NotBlank(message = "El nombre del campo es requerido")
    private String fieldName;
}
