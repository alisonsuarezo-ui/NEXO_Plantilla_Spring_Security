package com.roles.usermanagement.modules.publiccatalog;

import com.roles.usermanagement.modules.category.CategoryResponse;
import com.roles.usermanagement.modules.category.CategoryService;
import com.roles.usermanagement.modules.promotion.PromotionResponse;
import com.roles.usermanagement.modules.promotion.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Rutas de solo lectura, sin autenticación. Solo exponen datos activos. */
@RestController
@RequestMapping("/api/public")
@Tag(name = "Público")
public class PublicCatalogController {

 private final CategoryService categories;
 private final PromotionService promotions;

 public PublicCatalogController(CategoryService categories, PromotionService promotions) {
  this.categories = categories;
  this.promotions = promotions;
 }

 @Operation(summary = "Categorías activas", security = {})
 @GetMapping("/categories")
 public List<CategoryResponse> categories() {
  return categories.publicActive();
 }

 @Operation(summary = "Promociones vigentes hoy", security = {})
 @GetMapping("/promotions")
 public List<PromotionResponse> promotions() {
  return promotions.publicCurrent();
 }
}
