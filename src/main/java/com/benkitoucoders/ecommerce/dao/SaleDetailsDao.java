package com.benkitoucoders.ecommerce.dao;


import com.benkitoucoders.ecommerce.dtos.SaleDetailsDto;
import com.benkitoucoders.ecommerce.entities.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDetailsDao extends JpaRepository<SaleDetails, Long>, JpaSpecificationExecutor<SaleDetails> {
    @Query(value = "select new com.benkitoucoders.ecommerce.dtos.SaleDetailsDto(" +
            " saleDetails.id, saleDetails.productId,product.name, saleDetails.quantity,saleDetails.price, saleDetails.saleId, " +
            "saleDetails.dateCreation, saleDetails.dateUpdate)" +
            " FROM SaleDetails saleDetails " +
            " INNER JOIN Product product on product.id=saleDetails.productId" +
            " WHERE (:id IS NULL OR saleDetails.id = :id)" +
            "AND (:saleId IS NULL OR saleDetails.saleId = :saleId) ")
    List<SaleDetailsDto> getSaleDetailsByQuery(
            @Param("id") Long id,
            @Param("saleId") Long saleId
    );
}
