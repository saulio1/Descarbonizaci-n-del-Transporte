package dev.saul.descarbonizacion_transporte.modules.worker_positions.controller;

import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.input.WorkerPositionInputDto;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.output.WorkerPositionOutputDto;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.service.WorkerPositionService;
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

@Tag(name = "Ocupaciones", description = "Endpoints para gestionar las ocupaciones de los trabajadores")
@RestController
@RequestMapping("/api/positions")
@Validated
public class WorkerPositionController {
    private final WorkerPositionService service;

    public WorkerPositionController(WorkerPositionService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todas las ocupaciones activas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ocupaciones", content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkerPositionOutputDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @GetMapping
    public ResponseEntity<List<WorkerPositionOutputDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener ocupación por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ocupación encontrada", content = @Content(schema = @Schema(implementation = WorkerPositionOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Ocupación no encontrada", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkerPositionOutputDto> getById(
            @Parameter(description = "ID de la ocupación") @PathVariable String id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nueva ocupación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ocupación creada", content = @Content(schema = @Schema(implementation = WorkerPositionOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<WorkerPositionOutputDto> createUser(
            @Parameter(description = "Datos de la nueva ocupación") @Valid @RequestBody WorkerPositionInputDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar ocupación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ocupación actualizada", content = @Content(schema = @Schema(implementation = WorkerPositionOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Ocupación no encontrada", content = @Content())
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> patchUser(
            @Parameter(description = "ID de la ocupación") @PathVariable String id,
            @Parameter(description = "Campos a actualizar") @Valid @RequestBody WorkerPositionInputDto dto) {
        try {
            return ResponseEntity.ok(service.put(id, dto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Ocupación no encontrada"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar ocupación lógicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ocupación eliminada", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Ocupación no encontrada", content = @Content())
    })
    @DeleteMapping("/{id}/logical")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> deleteLogical(
            @Parameter(description = "ID de la ocupación") @PathVariable String id) {
        try {
            service.deleteLogical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Ocupación no encontrada"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar ocupación físicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ocupación eliminada", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Ocupación no encontrada", content = @Content())
    })
    @DeleteMapping("/{id}/physical")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> deletePhysical(
            @Parameter(description = "ID de la ocupación") @PathVariable String id) {
        try {
            service.deletePhysical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Ocupación no encontrada"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}