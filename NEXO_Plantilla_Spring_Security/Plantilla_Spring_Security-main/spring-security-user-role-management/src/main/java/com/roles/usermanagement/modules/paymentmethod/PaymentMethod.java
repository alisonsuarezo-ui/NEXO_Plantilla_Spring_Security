package com.roles.usermanagement.modules.paymentmethod;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "business_payment_method")
@Getter
@Setter
public class PaymentMethod {

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
