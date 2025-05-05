package dev.saul.descarbonizacion_transporte.modules.equipments.dto.input;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentUsageDataInputDto {
    private List<EquipmentUsageDataFInputDto> usageData;
}
