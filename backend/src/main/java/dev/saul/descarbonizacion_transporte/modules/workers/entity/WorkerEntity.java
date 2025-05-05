package dev.saul.descarbonizacion_transporte.modules.workers.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.saul.descarbonizacion_transporte.global.entity.GenericEntity;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.entity.WorkerPositionEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "workers")
public class WorkerEntity extends GenericEntity {
    @NotBlank
    @Size(max = 60)
    private String names;

    @NotBlank
    @Size(max = 100)
    private String lastNames;

    @NotBlank
    @Size(max = 11)
    private String cid;

    @NotBlank
    @Size(max = 30)
    private String phoneNumber;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @DBRef
    private WorkerPositionEntity position;
}
