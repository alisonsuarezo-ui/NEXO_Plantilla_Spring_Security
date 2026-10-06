package com.roles.usermanagement.modules.product;

import com.roles.usermanagement.modules.category.Category;
import com.roles.usermanagement.modules.category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProductService {

 private final ProductRepository repository;
 private final CategoryRepository categories;

 public ProductService(ProductRepository repository, CategoryRepository categories) {
  this.repository = repository;
  this.categories = categories;
 }

 private Category category(Long categoryId) {
  if (categoryId == null) return null;
  Category c = categories.findById(categoryId)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
  if (!c.isActive()) {
   throw new ResponseStatusException(HttpStatus.CONFLICT, "Categoría inactiva");
  }
  return c;
 }

 private Product existing(Long id, boolean lock) {
  return (lock ? repository.findForUpdate(id) : repository.findById(id))
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));
 }

 private ProductResponse dto(Product e) {
  return new ProductResponse(
          e.getId(),
          e.getName(),
          e.getSku(),
          e.getPrice(),
          e.getStock(),
          e.isActive(),
          e.getCategory() == null ? null : e.getCategory().getId(),
          e.getCategory() == null ? null : e.getCategory().getName()
  );
 }

 @Transactional(readOnly = true)
 public Page<ProductResponse> all(int page, int size, Long categoryId) {
  if (page < 0 || size < 1 || size > 100) {
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page >= 0; size entre 1 y 100");
  }
  PageRequest pageable = PageRequest.of(page, size, Sort.by("id"));
  return (categoryId == null
          ? repository.findAll(pageable)
          : repository.findByCategoryId(categoryId, pageable)).map(this::dto);
 }

 @Transactional(readOnly = true)
 public ProductResponse get(Long id) {
  return dto(existing(id, false));
 }

 public ProductResponse create(ProductRequest d) {
  Product e = new Product();
  e.setName(d.name().trim());
  e.setSku(d.sku().trim());
  e.setPrice(d.price());
  e.setStock(d.stock());
  e.setCategory(category(d.categoryId()));
  return dto(repository.save(e));
 }

 public ProductResponse update(Long id, ProductRequest d) {
  Product e = existing(id, true);
  if (!e.isActive()) {
   throw new ResponseStatusException(HttpStatus.CONFLICT, "Registro inactivo");
  }
  e.setName(d.name().trim());
  e.setSku(d.sku().trim());
  e.setPrice(d.price());
  e.setStock(d.stock());
  e.setCategory(category(d.categoryId()));
  return dto(e);
 }

 public void deactivate(Long id) {
  existing(id, true).setActive(false);
 }
}