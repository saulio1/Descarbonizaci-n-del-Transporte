package dev.saul.descarbonizacion_transporte.modules.equipments.dto.output;

import java.util.List;

import dev.saul.descarbonizacion_transporte.modules.equipments.entity.EquipmentDataField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.DataField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeEquipmentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentOutputDto {
    private String id;
    private String name;
    private String description;
    private List<EquipmentDataField> data;
    private List<DataField> otherDataFields;
    private TypeEquipmentEntity typeEquipment;
    private String usageDataEnergyConsumedMeasurementUnit;
    private String usageDataLoadOrCapacityUsedMeasurementUnit;
}
