package dev.saul.descarbonizacion_transporte.modules.equipments.dto.input;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentUsageDataFInputDto {
    @NotNull(message = "El id es requerido")
    private String id;

    @NotNull(message = "El inicio del uso es requerido")
    private LocalDateTime usageStart;

    @NotNull(message = "El fin del uso es requerido")
    private LocalDateTime usageEnd;

    @NotNull(message = "Las horas operando es requerido")
    private double operatingHours;

    @NotNull(message = "La energía consumida es requerida")
    private double energyConsumed;

    @NotNull(message = "La capacidad o carga usada es requerida")
    private double loadOrCapacityUsed;

    @NotBlank(message = "La localización es requerida")
    private String location;

    @NotBlank(message = "El operador es requerido")
    private String operator;

    private String notes;
}
