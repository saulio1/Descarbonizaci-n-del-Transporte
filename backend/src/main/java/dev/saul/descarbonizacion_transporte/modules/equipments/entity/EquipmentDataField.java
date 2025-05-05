package dev.saul.descarbonizacion_transporte.modules.equipments.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDataField {
    private String data;

    private String fieldName;
}
