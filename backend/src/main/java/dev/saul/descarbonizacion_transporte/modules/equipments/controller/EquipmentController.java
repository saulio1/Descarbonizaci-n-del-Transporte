package dev.saul.descarbonizacion_transporte.modules.equipments.controller;

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

import dev.saul.descarbonizacion_transporte.modules.equipments.dto.input.EquipmentInputDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.dto.input.EquipmentUsageDataInputDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.dto.output.EquipmentOutputDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.dto.output.EquipmentOutputUsageDataDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.service.EquipmentService;

import java.util.List;

@Tag(name = "Equipamientos", description = "Endpoints para gestionar los equipamientos")
@RestController
@RequestMapping("/api/equipments")
@Validated
public class EquipmentController {
    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los equipamientos activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de equipamientos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentOutputDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content())
    })
    @GetMapping
    public ResponseEntity<List<EquipmentOutputDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener datos de uso del equipamiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de uso encontrados", content = @Content(schema = @Schema(implementation = EquipmentOutputUsageDataDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Equipamiento no encontrado", content = @Content())
    })
    @GetMapping("/usage-data/{id}")
    public ResponseEntity<EquipmentOutputUsageDataDto> getUsageDataById(
            @Parameter(description = "ID del equipamiento") @PathVariable String id) {
        try {
            return ResponseEntity.ok(service.findByIdUsageData(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener equipamiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipamiento encontrado", content = @Content(schema = @Schema(implementation = EquipmentOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Equipamiento no encontrado", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentOutputDto> getById(
            @Parameter(description = "ID del equipamiento") @PathVariable String id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo equipamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipamiento creado", content = @Content(schema = @Schema(implementation = EquipmentOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Tipo de Equipamiento no encontrado", content = @Content())
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<EquipmentOutputDto> create(
            @Parameter(description = "Datos del nuevo tipo de equipamiento") @Valid @RequestBody EquipmentInputDto dto) {
        try {
            return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar datos de uso del equipamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de uso del equipamiento actualizado", content = @Content(schema = @Schema(implementation = EquipmentOutputUsageDataDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Equipamiento no encontrado", content = @Content())
    })
    @PutMapping("usage-data/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> putUsageData(
            @Parameter(description = "ID del equipamiento") @PathVariable String id,
            @Parameter(description = "Datos de uso") @Valid @RequestBody EquipmentUsageDataInputDto dto) {
        try {
            return ResponseEntity.ok(service.putUsageData(id, dto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Equipamiento no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar equipamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipamiento actualizado", content = @Content(schema = @Schema(implementation = EquipmentOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Equipamiento no encontrado", content = @Content())
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> put(
            @Parameter(description = "ID del equipamiento") @PathVariable String id,
            @Parameter(description = "Campos a actualizar") @Valid @RequestBody EquipmentInputDto dto) {
        try {
            return ResponseEntity.ok(service.put(id, dto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Equipamiento no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar equipamiento lógicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipamiento eliminado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Equipamiento no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}/logical")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> deleteLogical(
            @Parameter(description = "ID del equipamiento") @PathVariable String id) {
        try {
            service.deleteLogical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Equipamiento no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar equipamiento físicamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipamiento eliminado", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content()),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Equipamiento no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}/physical")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> deletePhysical(
            @Parameter(description = "ID del equipamiento") @PathVariable String id) {
        try {
            service.deletePhysical(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Equipamiento no encontrado"))
                return ResponseEntity.notFound().build();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
