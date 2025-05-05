package dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerPositionOutputDto {
    private String id;

    private String name;
}
