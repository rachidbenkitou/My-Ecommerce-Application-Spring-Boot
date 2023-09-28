package com.benkitoumiraouycoders.ecommerce.dao;

import com.benkitoumiraouycoders.ecommerce.dtos.ClientOrderDetailsDto;
import com.benkitoumiraouycoders.ecommerce.entities.ClientOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientOrderDetailsDao extends JpaRepository<ClientOrderDetails, Long>, JpaSpecificationExecutor<ClientOrderDetails> {
    @Query("SELECT NEW com.benkitoumiraouycoders.ecommerce.dtos.ClientOrderDetailsDto(c.id, c.clientOrderId," +
            " c.productId, p.name, c.price," +
            "c.quantity) " +
            "FROM ClientOrderDetails c " +
            "LEFT JOIN  Product p ON c.productId = p.id ")
    List<ClientOrderDetailsDto> findAllClientOrderDetails();
}
