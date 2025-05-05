package dev.saul.descarbonizacion_transporte.modules.reports.dto.output;

import java.util.List;

import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentUsageReportOutputDto {
    private List<EquipmentEntity> data;
}
