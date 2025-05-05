package dev.saul.descarbonizacion_transporte.modules.users.dto.input;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchDto {
    @Size(max = 30, message = "El username debe tener como máximo 30 caracteres")
    private String username;

    @Email(message = "El email debe ser válido")
    @Size(max = 50, message = "El email debe tener como máximo 50 caracteres")
    private String email;

    @Size(max = 120, message = "La contraseña debe tener como máximo 120 caracteres")
    private String password;

    private Set<String> roles;
}
