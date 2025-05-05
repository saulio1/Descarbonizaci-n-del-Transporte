package dev.saul.descarbonizacion_transporte.modules.worker_positions.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import dev.saul.descarbonizacion_transporte.global.entity.GenericEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "workerPositions")
public class WorkerPositionEntity extends GenericEntity {
    private String name;
}
