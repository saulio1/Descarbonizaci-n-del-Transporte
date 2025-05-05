package dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.input;

import dev.saul.descarbonizacion_transporte.global.validator.UniqueField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerPositionInputDto {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 30, message = "El nombre debe tener como máximo 30 caracteres")
    @UniqueField(collection = "workerPositions", fieldName = "name", message = "El nombre ya está en uso")
    private String name;
}
