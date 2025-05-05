package dev.saul.descarbonizacion_transporte.modules.type_equipments.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataField {
    private String name;
    private String description;
    private int minimum;
    private int maximum;
    private LocalDateTime minimumDateTime;
    private LocalDateTime maximumDateTime;
    private List<String> options;
    private boolean required;
    private TypeField typeField;

}
