package dev.saul.descarbonizacion_transporte.modules.users.dto.input;

import java.util.Set;

import dev.saul.descarbonizacion_transporte.global.validator.UniqueField;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDto {
    @NotBlank(message = "El username es requerido")
    @Size(max = 30, message = "El username debe tener como máximo 30 caracteres")
    @UniqueField(collection = "users", fieldName = "username", message = "El username ya está en uso")
    private String username;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @Size(max = 50, message = "El email debe tener como máximo 50 caracteres")
    @UniqueField(collection = "users", fieldName = "email", message = "El email ya está en uso")
    private String email;

    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 6, max = 120, message = "La nueva contraseña debe tener entre 6 y 120 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "La nueva contraseña debe contener al menos una letra mayúscula y un número")
    private String password;

    @NotEmpty(message = "Los roles son requeridos")
    private Set<String> roles;
}