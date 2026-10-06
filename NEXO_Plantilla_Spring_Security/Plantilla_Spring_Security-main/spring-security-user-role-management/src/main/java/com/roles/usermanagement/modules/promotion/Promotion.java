package com.roles.usermanagement.modules.promotion;

import com.roles.usermanagement.modules.product.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "business_promotion")
@Getter
@Setter
public class Promotion {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(nullable = false, length = 150)
 private String name;

 @Column(length = 500)
 private String description;

 @Column(nullable = false, precision = 5, scale = 2)
 private BigDecimal discount;

 @Column(nullable = false)
 private LocalDate startDate;

 @Column(nullable = false)
 private LocalDate endDate;

 @Column(nullable = false)
 private boolean active = true;

 @ManyToMany
 @JoinTable(name = "business_promotion_product",
         joinColumns = @JoinColumn(name = "promotion_id"),
         inverseJoinColumns = @JoinColumn(name = "product_id"))
 private Set<Product> products = new LinkedHashSet<>();
}
