package dev.saul.descarbonizacion_transporte.modules.reports.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastEquipmentsReportOutputDto {
    private String id;
    private String name;
    private String description;
    private String typeEquipment;
}
