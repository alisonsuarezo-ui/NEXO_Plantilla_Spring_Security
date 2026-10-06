package com.roles.usermanagement.modules.paymentmethod;

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
@RequestMapping("/api/payment-methods")
@Tag(name = "Métodos de pago")
@SecurityRequirement(name = "bearerAuth")
public class PaymentMethodController {

 private final PaymentMethodService service;

 public PaymentMethodController(PaymentMethodService service) {
  this.service = service;
 }

 @Operation(summary = "Listar métodos de pago", description = "Paginación: page desde 0; size entre 1 y 100. Incluye inactivos.")
 @GetMapping
 @PreAuthorize("hasAuthority('PAYMENT_METHOD_READ')")
 public Page<PaymentMethodResponse> all(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
  return service.all(page, size);
 }

 @Operation(summary = "Consultar detalle")
 @GetMapping("/{id}")
 @PreAuthorize("hasAuthority('PAYMENT_METHOD_READ')")
 public PaymentMethodResponse get(@PathVariable Long id) {
  return service.get(id);
 }

 @Operation(summary = "Crear registro")
 @PostMapping
 @PreAuthorize("hasAuthority('PAYMENT_METHOD_CREATE')")
 public ResponseEntity<PaymentMethodResponse> create(@Valid @RequestBody PaymentMethodRequest dto) {
  return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
 }

 @Operation(summary = "Actualizar registro")
 @PutMapping("/{id}")
 @PreAuthorize("hasAuthority('PAYMENT_METHOD_UPDATE')")
 public PaymentMethodResponse update(@PathVariable Long id, @Valid @RequestBody PaymentMethodRequest dto) {
  return service.update(id, dto);
 }

 @Operation(summary = "Desactivar registro", description = "No elimina físicamente; conserva el historial.")
 @DeleteMapping("/{id}")
 @PreAuthorize("hasAuthority('PAYMENT_METHOD_DELETE')")
 public ResponseEntity<Void> deactivate(@PathVariable Long id) {
  service.deactivate(id);
  return ResponseEntity.noContent().build();
 }
}
