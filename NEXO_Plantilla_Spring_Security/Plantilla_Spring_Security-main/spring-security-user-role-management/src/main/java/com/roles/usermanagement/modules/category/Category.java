package com.roles.usermanagement.modules.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "business_category")
@Getter
@Setter
public class Category {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(nullable = false, unique = true, length = 100)
 private String name;

 @Column(length = 300)
 private String description;

 @Column(length = 50)
 private String icon;

 @Column(nullable = false)
 private boolean active = true;
}
