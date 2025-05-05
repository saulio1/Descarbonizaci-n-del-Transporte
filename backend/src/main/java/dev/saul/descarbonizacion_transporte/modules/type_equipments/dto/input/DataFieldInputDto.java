package dev.saul.descarbonizacion_transporte.modules.type_equipments.dto.input;

import java.time.LocalDateTime;
import java.util.List;

import dev.saul.descarbonizacion_transporte.global.validator.UniqueField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataFieldInputDto {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 30, message = "El nombre debe tener como máximo 30 caracteres")
    private String name;

    @Size(max = 50, message = "La descripción debe tener como máximo 50 caracteres")
    private String description;

    private int minimum;
    private int maximum;
    private LocalDateTime minimumDateTime;
    private LocalDateTime maximumDateTime;
    private List<String> options;

    @NotNull(message = "Es necesario especificar si el campo es requerido")
    private boolean required;

    @NotNull(message = "El tipo de campo es requerido")
    private String typeField;
}
