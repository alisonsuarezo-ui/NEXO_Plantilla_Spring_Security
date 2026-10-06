package com.roles.usermanagement.modules.category;

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
@RequestMapping("/api/categories")
@Tag(name = "Categorías")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

 private final CategoryService service;

 public CategoryController(CategoryService service) {
  this.service = service;
 }

 @Operation(summary = "Listar categorías", description = "Paginación: page desde 0; size entre 1 y 100. Incluye inactivos.")
 @GetMapping
 @PreAuthorize("hasAuthority('CATEGORY_READ')")
 public Page<CategoryResponse> all(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
  return service.all(page, size);
 }

 @Operation(summary = "Consultar detalle")
 @GetMapping("/{id}")
 @PreAuthorize("hasAuthority('CATEGORY_READ')")
 public CategoryResponse get(@PathVariable Long id) {
  return service.get(id);
 }

 @Operation(summary = "Crear registro")
 @PostMapping
 @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
 public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest dto) {
  return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
 }

 @Operation(summary = "Actualizar registro")
 @PutMapping("/{id}")
 @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
 public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest dto) {
  return service.update(id, dto);
 }

 @Operation(summary = "Desactivar registro", description = "No elimina físicamente; conserva el historial.")
 @DeleteMapping("/{id}")
 @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
 public ResponseEntity<Void> deactivate(@PathVariable Long id) {
  service.deactivate(id);
  return ResponseEntity.noContent().build();
 }
}
