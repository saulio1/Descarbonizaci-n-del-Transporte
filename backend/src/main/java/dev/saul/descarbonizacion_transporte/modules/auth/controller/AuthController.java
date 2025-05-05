package dev.saul.descarbonizacion_transporte.modules.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.saul.descarbonizacion_transporte.modules.auth.dto.input.ChangePasswordDto;
import dev.saul.descarbonizacion_transporte.modules.auth.dto.input.LoginInputDto;
import dev.saul.descarbonizacion_transporte.modules.auth.dto.output.LoginOutputDto;
import dev.saul.descarbonizacion_transporte.modules.auth.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Autenticación", description = "Endpoints de autenticación")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Iniciar sesión", description = "Autenticar un usuario usando username o email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(schema = @Schema(implementation = LoginOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content())
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Parameter(description = "Datos de autenticación") @RequestBody LoginInputDto loginInputDto) {
        try {
            LoginOutputDto response = authService.login(loginInputDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    "Credenciales no válidas",
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Cambiar contraseña", description = "Actualizar la contraseña del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Error en la validación", content = @Content())
    })
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @Parameter(description = "Datos para cambiar la contraseña") @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            authService.changePassword(changePasswordDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}