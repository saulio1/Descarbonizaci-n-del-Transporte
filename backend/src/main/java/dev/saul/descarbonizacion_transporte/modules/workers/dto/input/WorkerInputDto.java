package dev.saul.descarbonizacion_transporte.modules.workers.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import dev.saul.descarbonizacion_transporte.global.validator.UniqueField;
import dev.saul.descarbonizacion_transporte.global.validator.ValidCarnet;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerInputDto {
    @NotBlank(message = "El nombre es requerido")
    private String names;

    @NotBlank(message = "Los apellidos son requeridos")
    private String lastNames;

    @NotBlank(message = "El teléfono es requerido")
    @Pattern(regexp = "(\\+53\\s)?\\d{8}", message = "El teléfono debe tener el formato '+53 xxxxxxxx' o 'xxxxxxxx'")
    private String phoneNumber;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo no es válido")
    @UniqueField(collection = "workers", fieldName = "email", message = "El correo ya está en uso")
    private String email;

    @NotBlank(message = "La ocupación del trabajador no puede estar en blanco")
    private String positionId;

    @NotBlank(message = "El carnet es requerido")
    @ValidCarnet
    @UniqueField(collection = "workers", fieldName = "cid", message = "El carnet de identidad ya está en uso")
    private String cid;

}
