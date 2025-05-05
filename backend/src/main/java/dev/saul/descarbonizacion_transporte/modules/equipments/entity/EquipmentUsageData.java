package dev.saul.descarbonizacion_transporte.modules.equipments.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentUsageData {
    @Id
    private String id;
    private LocalDateTime usageStart;
    private LocalDateTime usageEnd;

    private LocalDateTime date;

    private double operatingHours;
    private double energyConsumed;
    private double loadOrCapacityUsed;
    private String location;
    private String operator;
    private String notes;
}
