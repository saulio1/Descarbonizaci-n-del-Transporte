package dev.saul.descarbonizacion_transporte.modules.equipments.dto.output;

import java.util.List;

import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentUsageData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentOutputUsageDataDto {
    private List<EquipmentUsageData> usageData;
}
