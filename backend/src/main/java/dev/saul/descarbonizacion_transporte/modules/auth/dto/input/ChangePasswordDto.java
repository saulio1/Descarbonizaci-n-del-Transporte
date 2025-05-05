package dev.saul.descarbonizacion_transporte.modules.auth.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    @NotBlank(message = "La contraseña actual es requerida")
    private String currentPassword;

    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 6, max = 120, message = "La nueva contraseña debe tener entre 6 y 120 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "La nueva contraseña debe contener al menos una letra mayúscula y un número")
    private String newPassword;
}