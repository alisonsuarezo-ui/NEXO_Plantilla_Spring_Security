package com.roles.usermanagement.modules.promotion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotions")
@Tag(name = "Promociones")
@SecurityRequirement(name = "bearerAuth")
public class PromotionController {

 private final PromotionService service;

 public PromotionController(PromotionService service) {
  this.service = service;
 }

 @Operation(summary = "Listar promociones", description = "Paginación: page desde 0; size entre 1 y 100. Incluye inactivos.")
 @GetMapping
 @PreAuthorize("hasAuthority('PROMOTION_READ')")
 public Page<PromotionResponse> all(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
  return service.all(page, size);
 }

 @Operation(summary = "Consultar detalle")
 @GetMapping("/{id}")
 @PreAuthorize("hasAuthority('PROMOTION_READ')")
 public PromotionResponse get(@PathVariable Long id) {
  return service.get(id);
 }

 @Operation(summary = "Crear registro")
 @PostMapping
 @PreAuthorize("hasAuthority('PROMOTION_CREATE')")
 public ResponseEntity<PromotionResponse> create(@Valid @RequestBody PromotionRequest dto) {
  return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
 }

 @Operation(summary = "Actualizar registro")
 @PutMapping("/{id}")
 @PreAuthorize("hasAuthority('PROMOTION_UPDATE')")
 public PromotionResponse update(@PathVariable Long id, @Valid @RequestBody PromotionRequest dto) {
  return service.update(id, dto);
 }

 @Operation(summary = "Desactivar registro", description = "No elimina físicamente; conserva el historial.")
 @DeleteMapping("/{id}")
 @PreAuthorize("hasAuthority('PROMOTION_DELETE')")
 public ResponseEntity<Void> deactivate(@PathVariable Long id) {
  service.deactivate(id);
  return ResponseEntity.noContent().build();
 }
}
