package com.roles.usermanagement.modules.promotion;

import com.roles.usermanagement.modules.product.Product;
import com.roles.usermanagement.modules.product.ProductRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PromotionService {

 private final PromotionRepository repository;
 private final ProductRepository products;

 public PromotionService(PromotionRepository repository, ProductRepository products) {
  this.repository = repository;
  this.products = products;
 }

 private Promotion existing(Long id) {
  return repository.findById(id)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promoción no encontrada"));
 }

 PromotionResponse dto(Promotion e) {
  LocalDate today = LocalDate.now();
  String status;
  if (!e.isActive()) status = "INACTIVA";
  else if (today.isBefore(e.getStartDate())) status = "PROGRAMADA";
  else if (today.isAfter(e.getEndDate())) status = "FINALIZADA";
  else status = "ACTIVA";

  long total = Math.max(1, ChronoUnit.DAYS.between(e.getStartDate(), e.getEndDate()));
  long elapsed = ChronoUnit.DAYS.between(e.getStartDate(), today);
  int progress = (int) Math.max(0, Math.min(100, elapsed * 100 / total));

  return new PromotionResponse(e.getId(), e.getName(), e.getDescription(), e.getDiscount(),
          e.getStartDate(), e.getEndDate(), e.isActive(), status, progress,
          e.getProducts().stream()
                  .map(p -> new PromotionResponse.ProductRef(p.getId(), p.getName()))
                  .toList());
 }

 @Transactional(readOnly = true)
 public Page<PromotionResponse> all(int page, int size) {
  if (page < 0 || size < 1 || size > 100) {
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page >= 0; size entre 1 y 100");
  }
  return repository.findAll(PageRequest.of(page, size, Sort.by("id").descending())).map(this::dto);
 }

 @Transactional(readOnly = true)
 public PromotionResponse get(Long id) {
  return dto(existing(id));
 }

 /** Promociones activas y vigentes hoy; las usa la ruta pública. */
 @Transactional(readOnly = true)
 public List<PromotionResponse> publicCurrent() {
  LocalDate today = LocalDate.now();
  return repository
          .findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByEndDateAsc(today, today)
          .stream().map(this::dto).toList();
 }

 public PromotionResponse create(PromotionRequest d) {
  Promotion e = new Promotion();
  apply(e, d);
  return dto(repository.save(e));
 }

 public PromotionResponse update(Long id, PromotionRequest d) {
  Promotion e = existing(id);
  apply(e, d);
  return dto(e);
 }

 public void deactivate(Long id) {
  existing(id).setActive(false);
 }

 private void apply(Promotion e, PromotionRequest d) {
  if (d.endDate().isBefore(d.startDate())) {
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha fin no puede ser anterior al inicio");
  }
  Set<Product> selected = new LinkedHashSet<>();
  if (d.productIds() != null) {
   for (Long productId : d.productIds()) {
    Product p = products.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    if (!p.isActive()) {
     throw new ResponseStatusException(HttpStatus.CONFLICT, "Producto inactivo");
    }
    selected.add(p);
   }
  }
  e.setName(d.name().trim());
  e.setDescription(d.description() == null ? null : d.description().trim());
  e.setDiscount(d.discount());
  e.setStartDate(d.startDate());
  e.setEndDate(d.endDate());
  e.getProducts().clear();
  e.getProducts().addAll(selected);
 }
}
