package dev.saul.descarbonizacion_transporte.modules.workers.controller;

import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.output.WorkerPositionOutputDto;
import dev.saul.descarbonizacion_transporte.modules.workers.dto.input.WorkerInputDto;
import dev.saul.descarbonizacion_transporte.modules.workers.dto.input.WorkerPatchDto;
import dev.saul.descarbonizacion_transporte.modules.workers.dto.output.WorkerOutputDto;
import dev.saul.descarbonizacion_transporte.modules.workers.service.WorkerService;
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

@Tag(name = "Trabajadores", description = "Endpoints para gestionar trabajadores")
@RestController
@RequestMapping("/api/workers")
@Validated
public class WorkerController {
    private final WorkerService service;

    public WorkerController(WorkerService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los trabajadores activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de trabajadores", content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkerOutputDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<List<WorkerOutputDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener trabajador por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajador encontrado", content = @Content(schema = @Schema(implementation = WorkerOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado", content = @Content())
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<WorkerOutputDto> getUserById(
            @Parameter(description = "ID del trabajador") @PathVariable String id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo trabajador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trabajador creado", content = @Content(schema = @Schema(implementation = WorkerOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<WorkerOutputDto> createUser(
            @Parameter(description = "Datos del nuevo trabajador") @Valid @RequestBody WorkerInputDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar trabajador (PATCH)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajador actualizado", content = @Content(schema = @Schema(implementation = WorkerOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado", content = @Content())
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> patchUser(
            @Parameter(description = "ID del trabajador") @PathVariable String id,
            @Parameter(description = "Campos a actualizar") @Valid @RequestBody WorkerPatchDto patchDto) {
        try {
            return ResponseEntity.ok(service.patch(id, patchDto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Trabajador no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar trabajador lógicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trabajador eliminado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}/logical")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> deleteLogical(
            @Parameter(description = "ID del trabajador") @PathVariable String id) {
        try {
            service.deleteLogical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Trabajador no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar trabajador físicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trabajador eliminado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}/physical")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> deletePhysical(
            @Parameter(description = "ID del trabajador") @PathVariable String id) {
        try {
            service.deletePhysical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Trabajador no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
