package dev.saul.descarbonizacion_transporte.modules.workers.dto.input;

import dev.saul.descarbonizacion_transporte.global.validator.ValidCarnet;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerPatchDto {
    private String names;

    private String lastNames;

    @Pattern(regexp = "(\\+53\\s)?\\d{8}", message = "El teléfono debe tener el formato '+53 xxxxxxxx' o 'xxxxxxxx'")
    private String phoneNumber;

    @Email(message = "El correo no es válido")
    private String email;

    private String positionId;

    @ValidCarnet
    private String cid;
}
