package dev.saul.descarbonizacion_transporte.modules.auth.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInputDto {
    private String usernameOrEmail;
    private String password;
}