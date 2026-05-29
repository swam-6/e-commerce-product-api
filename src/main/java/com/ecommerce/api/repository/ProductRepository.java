package com.ecommerce.api.repository;

import com.ecommerce.api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    // Filter by category
    Page<Product> findByCategoryId(
            Long categoryId,
            Pageable pageable
    );

    // Search by name
    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    // Search with multiple filters combined
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%'))) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:categoryId IS NULL OR " +
            "p.category.id = :categoryId)")
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    // Find low stock products
    List<Product> findByStockQuantityLessThan(
            Integer threshold
    );
}