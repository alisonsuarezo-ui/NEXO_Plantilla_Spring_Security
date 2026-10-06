package com.roles.usermanagement.modules.product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface ProductRepository extends JpaRepository<Product,Long> {
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select e from Product e where e.id=:id")
 Optional<Product> findForUpdate(@Param("id") Long id);
 Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
 long countByCategoryIdAndActiveTrue(Long categoryId);
}
