package com.roles.usermanagement.modules.paymentmethod;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PaymentMethodService {

 private final PaymentMethodRepository repository;

 public PaymentMethodService(PaymentMethodRepository repository) {
  this.repository = repository;
 }

 private PaymentMethod existing(Long id) {
  return repository.findById(id)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));
 }

 private PaymentMethodResponse dto(PaymentMethod e) {
  return new PaymentMethodResponse(e.getId(), e.getName(), e.getDescription(), e.getIcon(), e.isActive());
 }

 @Transactional(readOnly = true)
 public Page<PaymentMethodResponse> all(int page, int size) {
  if (page < 0 || size < 1 || size > 100) {
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page >= 0; size entre 1 y 100");
  }
  return repository.findAll(PageRequest.of(page, size, Sort.by("id"))).map(this::dto);
 }

 @Transactional(readOnly = true)
 public PaymentMethodResponse get(Long id) {
  return dto(existing(id));
 }

 public PaymentMethodResponse create(PaymentMethodRequest d) {
  String name = d.name().trim();
  if (repository.existsByNameIgnoreCase(name)) {
   throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un método de pago con ese nombre");
  }
  PaymentMethod e = new PaymentMethod();
  apply(e, d, name);
  return dto(repository.save(e));
 }

 public PaymentMethodResponse update(Long id, PaymentMethodRequest d) {
  PaymentMethod e = existing(id);
  String name = d.name().trim();
  if (repository.existsByNameIgnoreCaseAndIdNot(name, id)) {
   throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un método de pago con ese nombre");
  }
  apply(e, d, name);
  return dto(e);
 }

 public void deactivate(Long id) {
  existing(id).setActive(false);
 }

 private void apply(PaymentMethod e, PaymentMethodRequest d, String name) {
  e.setName(name);
  e.setDescription(d.description() == null ? null : d.description().trim());
  e.setIcon(d.icon() == null ? null : d.icon().trim());
 }
}
