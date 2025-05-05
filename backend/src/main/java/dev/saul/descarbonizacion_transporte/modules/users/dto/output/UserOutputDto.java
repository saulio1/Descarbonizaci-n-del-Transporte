package dev.saul.descarbonizacion_transporte.modules.users.dto.output;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOutputDto {
    private String id;

    private String username;

    private String email;

    private Set<String> roles;
}
