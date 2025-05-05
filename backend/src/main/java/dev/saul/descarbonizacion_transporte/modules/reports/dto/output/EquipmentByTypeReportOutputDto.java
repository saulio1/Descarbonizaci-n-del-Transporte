package dev.saul.descarbonizacion_transporte.modules.reports.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentByTypeReportOutputDto {
    private int quantity;
    private String typeEquipment;
}
