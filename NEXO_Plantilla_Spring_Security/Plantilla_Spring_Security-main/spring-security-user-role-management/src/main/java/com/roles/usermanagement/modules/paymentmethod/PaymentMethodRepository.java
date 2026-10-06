package com.roles.usermanagement.modules.paymentmethod;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
 boolean existsByNameIgnoreCase(String name);
 boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
