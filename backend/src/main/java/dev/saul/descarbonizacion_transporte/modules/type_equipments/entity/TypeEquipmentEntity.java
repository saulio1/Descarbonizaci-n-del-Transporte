package dev.saul.descarbonizacion_transporte.modules.type_equipments.entity;

import dev.saul.descarbonizacion_transporte.global.entity.GenericEntity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "typeEquipments")
public class TypeEquipmentEntity extends GenericEntity {
    private String name;
    private String description;
    private List<DataField> dataFields;
}
