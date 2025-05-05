package dev.saul.descarbonizacion_transporte.modules.type_equipments.dto.output;

import java.util.List;

import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.DataField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeEquipmentOutputDto {
    private String id;
    private String name;
    private String description;
    private List<DataField> dataFields;
    private boolean isEditable;
}
