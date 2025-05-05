package dev.saul.descarbonizacion_transporte.modules.users.controller;

import dev.saul.descarbonizacion_transporte.modules.users.dto.input.UserInputDto;
import dev.saul.descarbonizacion_transporte.modules.users.dto.input.UserPatchDto;
import dev.saul.descarbonizacion_transporte.modules.users.dto.output.UserOutputDto;
import dev.saul.descarbonizacion_transporte.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Endpoints para gestionar usuarios")
@RestController
@RequestMapping("/api/users")
@Validated
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Obtener todos los usuarios activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserOutputDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserOutputDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Obtener usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = UserOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserOutputDto> getUserById(
            @Parameter(description = "ID del usuario") @PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.findUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = UserOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    @GetMapping("/me")
    public ResponseEntity<UserOutputDto> getMe() {
        try {
            return ResponseEntity.ok(userService.getMe());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado", content = @Content(schema = @Schema(implementation = UserOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserOutputDto> createUser(
            @Parameter(description = "Datos del nuevo usuario") @Valid @RequestBody UserInputDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar usuario (PATCH)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado", content = @Content(schema = @Schema(implementation = UserOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> patchUser(
            @Parameter(description = "ID del usuario") @PathVariable String id,
            @Parameter(description = "Campos a actualizar") @Valid @RequestBody UserPatchDto userPatchDto) {
        try {
            return ResponseEntity.ok(userService.patchUser(id, userPatchDto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Usuario no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar usuario lógicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}/logical")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteLogical(
            @Parameter(description = "ID del usuario") @PathVariable String id) {
        try {
            userService.deleteLogical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Usuario no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar usuario físicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}/physical")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deletePhysical(
            @Parameter(description = "ID del usuario") @PathVariable String id) {
        try {
            userService.deletePhysical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Usuario no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}