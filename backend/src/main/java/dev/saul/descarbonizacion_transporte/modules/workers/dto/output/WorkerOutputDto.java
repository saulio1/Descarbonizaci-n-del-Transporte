package dev.saul.descarbonizacion_transporte.modules.workers.dto.output;

import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.output.WorkerPositionOutputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerOutputDto {
    private String id;

    private String names;

    private String lastNames;

    private String cid;

    private String phoneNumber;

    private String email;

    private WorkerPositionOutputDto position;
}
