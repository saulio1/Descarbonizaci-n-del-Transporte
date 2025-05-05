package dev.saul.descarbonizacion_transporte.modules.equipments.entity;

import dev.saul.descarbonizacion_transporte.global.entity.GenericEntity;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.DataField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeEquipmentEntity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "equipments")
public class EquipmentEntity extends GenericEntity {
    private String name;
    private String description;
    private List<EquipmentDataField> data;
    private List<DataField> otherDataFields;
    private List<EquipmentUsageData> usageData;
    private String usageDataEnergyConsumedMeasurementUnit;
    private String usageDataLoadOrCapacityUsedMeasurementUnit;

    @DBRef
    private TypeEquipmentEntity typeEquipment;
}
