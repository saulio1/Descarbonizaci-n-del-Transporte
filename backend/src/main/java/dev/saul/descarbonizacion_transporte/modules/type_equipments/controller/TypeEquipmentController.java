package dev.saul.descarbonizacion_transporte.modules.type_equipments.controller;

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

import dev.saul.descarbonizacion_transporte.modules.type_equipments.dto.input.TypeEquipmentInputDto;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.dto.output.TypeEquipmentOutputDto;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.service.TypeEquipmentService;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.input.WorkerPositionInputDto;
import dev.saul.descarbonizacion_transporte.modules.worker_positions.dto.output.WorkerPositionOutputDto;

import java.util.List;

@Tag(name = "Tipos de Equipamiento", description = "Endpoints para gestionar los tipos de equipamientos")
@RestController
@RequestMapping("/api/typeEquipments")
@Validated
public class TypeEquipmentController {
    private final TypeEquipmentService service;

    public TypeEquipmentController(TypeEquipmentService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los tipos de equipamientos activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de equipamientos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TypeEquipmentOutputDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @GetMapping
    public ResponseEntity<List<TypeEquipmentOutputDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener equipamiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de equipamiento encontrado", content = @Content(schema = @Schema(implementation = TypeEquipmentOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Tipo de equipamiento no encontrado", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<TypeEquipmentOutputDto> getById(
            @Parameter(description = "ID del tipo de equipamiento") @PathVariable String id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo tipo de equipamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de equipamiento creado", content = @Content(schema = @Schema(implementation = TypeEquipmentOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<TypeEquipmentOutputDto> createUser(
            @Parameter(description = "Datos del nuevo tipo de equipamiento") @Valid @RequestBody TypeEquipmentInputDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar tipo de equipamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de equipamiento actualizado", content = @Content(schema = @Schema(implementation = TypeEquipmentOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Tipo de equipamiento no encontrado", content = @Content())
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> put(
            @Parameter(description = "ID del tipo de equipamiento") @PathVariable String id,
            @Parameter(description = "Campos a actualizar") @Valid @RequestBody TypeEquipmentInputDto dto) {
        try {
            return ResponseEntity.ok(service.put(id, dto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Tipo de equipamiento no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar tipo de equipamiento lógicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de equipamiento eliminado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Tipo de equipamiento no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}/logical")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> deleteLogical(
            @Parameter(description = "ID del tipo de equipamiento") @PathVariable String id) {
        try {
            service.deleteLogical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Tipo de equipamiento no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar tipo de equipamiento físicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de equipamiento eliminado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Tipo de equipamiento no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}/physical")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> deletePhysical(
            @Parameter(description = "ID de la tipo de equipamiento") @PathVariable String id) {
        try {
            service.deletePhysical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Tipo de equipamiento no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
