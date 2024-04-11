package com.benkitoucoders.ecommerce.dao;

import com.benkitoucoders.ecommerce.dtos.ProductDto;
import com.benkitoucoders.ecommerce.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query(value = "SELECT new com.benkitoucoders.ecommerce.dtos.ProductDto(" +
            " p.id, p.name, p.description, p.price, p.comparePrice, p.quantity, p.visibility, p.categoryId, c.name," +
            " p.dateCreated, p.dateUpdated, i.name, i.filePath, p.productOrder)" +
            " FROM Product p" +
            " LEFT JOIN Category c ON p.categoryId = c.id" +
            " LEFT JOIN Image i ON p.id = i.productId" +
            " WHERE (:id IS NULL OR p.id = :id)" +
            " AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))" +
            " AND (:price IS NULL OR p.price = :price)" +
            " AND (:quantity IS NULL OR p.quantity = :quantity)" +
            " AND (:visibility IS NULL OR p.visibility = :visibility)" +
            " AND (:categoryId IS NULL OR p.categoryId = :categoryId)" +
            " GROUP BY p.id  ")
        // Group by productId
    List<ProductDto> getProductsByQuery(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("price") Double price,
            @Param("quantity") Integer quantity,
            @Param("categoryId") Long categoryId,
            @Param("visibility") String visibility,
            Pageable pageable
    );

    @Query(value = "SELECT new com.benkitoucoders.ecommerce.dtos.ProductDto(" +
            " p.id, p.name, p.description, p.price, p.comparePrice, p.quantity, p.visibility, p.categoryId, c.name, p.dateCreated, p.dateUpdated, i.name, i.filePath)" +
            " FROM Product p" +
            " LEFT JOIN Category c ON p.categoryId = c.id" +
            " LEFT JOIN Image i ON p.id = i.productId" +
            " GROUP BY p.id order by p.dateCreated desc limit 15")
    List<ProductDto> getLastRecordedProductsByQuery(
    );

    @Query("SELECT new com.benkitoucoders.ecommerce.dtos.ProductDto(" +
            " p.id, p.name, p.description, p.price, p.comparePrice, p.quantity, p.visibility, p.categoryId, c.name, p.dateCreated, p.dateUpdated, i.name, i.filePath)" +
            " FROM Product p " +
            " LEFT JOIN Category c ON p.categoryId = c.id " +
            " LEFT JOIN Image i ON p.id = i.productId " +
            " WHERE p.id IN (" +
            "     SELECT cod.productId " +
            "     FROM ClientOrderDetails cod " +
            "     GROUP BY cod.productId " +
            "     ORDER BY SUM(cod.quantity) DESC " +
            "     LIMIT 15)" +
            " ORDER BY p.dateCreated DESC")
    List<ProductDto> getTop15MostOrderedProducts();
    boolean existsByName(String name);
}
