package dev.saul.descarbonizacion_transporte.modules.reports.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.saul.descarbonizacion_transporte.modules.equipments.dto.output.EquipmentOutputDto;
import dev.saul.descarbonizacion_transporte.modules.equipments.dto.output.EquipmentOutputUsageDataDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.EquipmentByTypeReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.EquipmentUsageReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.LastEquipmentsReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.LastWorkersReportOutDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.TotalEquipmentsReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.dto.output.TotalWorkersReportOutputDto;
import dev.saul.descarbonizacion_transporte.modules.reports.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reportes", description = "Endpoints para gestionar los reportes")
@RestController
@RequestMapping("/api/reports")
@Validated
public class ReportController {
    @Autowired
    private ReportService service;

    @Operation(summary = "Obtener los datos de uso de los equipamientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de uso encontrados", content = @Content(schema = @Schema(implementation = EquipmentUsageReportOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
    })
    @GetMapping("/usage-data")
    public ResponseEntity<EquipmentUsageReportOutputDto> getUsageDataReport(
            @Parameter(description = "IDs de los equipamientos. Por defecto se devuelven todos.") @RequestParam(required = false) Optional<List<String>> ids) {
        try {
            return ResponseEntity.ok(service.equipmentWithUsage(ids));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener los últimos 5 trabajadores ingresados al sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajadores encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LastWorkersReportOutDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
    })
    @GetMapping("/last-workers")
    public ResponseEntity<List<LastWorkersReportOutDto>> getLastWorkers() {
        try {
            return ResponseEntity.ok(service.lastWorkers());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener los últimos 5 equipamientos ingresados al sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipamientos encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LastEquipmentsReportOutputDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
    })
    @GetMapping("/last-equipments")
    public ResponseEntity<List<LastEquipmentsReportOutputDto>> getLastEquipments() {
        try {
            return ResponseEntity.ok(service.lastEquipments());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener el conteo de equipamientos por tipo de equipamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo de equipamientos por tipo de equipamiento", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentByTypeReportOutputDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
    })
    @GetMapping("/count-equipments")
    public ResponseEntity<List<EquipmentByTypeReportOutputDto>> getCountEquipmentsByType() {
        try {
            return ResponseEntity.ok(service.countEquipmentsByType());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener el total de equipamientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total de equipamientos", content = @Content(schema = @Schema(implementation = TotalEquipmentsReportOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
    })
    @GetMapping("/total/equipments")
    public ResponseEntity<TotalEquipmentsReportOutputDto> getTotalEquipments() {
        try {
            return ResponseEntity.ok(service.totalEquipments());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener el total de trabajadores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total de trabajadores", content = @Content(schema = @Schema(implementation = TotalWorkersReportOutputDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content()),
    })
    @GetMapping("/total/workers")
    public ResponseEntity<TotalWorkersReportOutputDto> getTotalWorkers() {
        try {
            return ResponseEntity.ok(service.totalWorkers());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
