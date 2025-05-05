package dev.saul.descarbonizacion_transporte.modules.auth.dto.output;

import java.util.Set;

import dev.saul.descarbonizacion_transporte.modules.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginOutputDto {
    private String token;
    private String id;
    private String username;
    private String email;
    private Set<Role> roles;
}
