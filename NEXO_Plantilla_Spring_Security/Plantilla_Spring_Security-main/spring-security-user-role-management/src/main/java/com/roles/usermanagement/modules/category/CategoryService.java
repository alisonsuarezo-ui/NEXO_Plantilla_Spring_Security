package com.roles.usermanagement.modules.category;

import com.roles.usermanagement.modules.product.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CategoryService {

 private final CategoryRepository repository;
 private final ProductRepository products;

 public CategoryService(CategoryRepository repository, ProductRepository products) {
  this.repository = repository;
  this.products = products;
 }

 private Category existing(Long id) {
  return repository.findById(id)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
 }

 CategoryResponse dto(Category e) {
  return new CategoryResponse(e.getId(), e.getName(), e.getDescription(), e.getIcon(), e.isActive(),
          products.countByCategoryIdAndActiveTrue(e.getId()));
 }

 @Transactional(readOnly = true)
 public Page<CategoryResponse> all(int page, int size) {
  if (page < 0 || size < 1 || size > 100) {
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page >= 0; size entre 1 y 100");
  }
  return repository.findAll(PageRequest.of(page, size, Sort.by("id"))).map(this::dto);
 }

 @Transactional(readOnly = true)
 public CategoryResponse get(Long id) {
  return dto(existing(id));
 }

 @Transactional(readOnly = true)
 public List<CategoryResponse> publicActive() {
  return repository.findByActiveTrueOrderByNameAsc().stream().map(this::dto).toList();
 }

 public CategoryResponse create(CategoryRequest d) {
  String name = d.name().trim();
  if (repository.existsByNameIgnoreCase(name)) {
   throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una categoría con ese nombre");
  }
  Category e = new Category();
  apply(e, d, name);
  return dto(repository.save(e));
 }

 public CategoryResponse update(Long id, CategoryRequest d) {
  Category e = existing(id);
  String name = d.name().trim();
  if (repository.existsByNameIgnoreCaseAndIdNot(name, id)) {
   throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una categoría con ese nombre");
  }
  apply(e, d, name);
  return dto(e);
 }

 public void deactivate(Long id) {
  existing(id).setActive(false);
 }

 private void apply(Category e, CategoryRequest d, String name) {
  e.setName(name);
  e.setDescription(d.description() == null ? null : d.description().trim());
  e.setIcon(d.icon() == null ? null : d.icon().trim());
 }
}
