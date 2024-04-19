package com.benkitoucoders.ecommerce.dao;

import com.benkitoucoders.ecommerce.dtos.ClientOrderDto;
import com.benkitoucoders.ecommerce.entities.ClientOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientOrderDao extends JpaRepository<ClientOrder, Long>, JpaSpecificationExecutor<ClientOrder> {
    @Query("SELECT NEW com.benkitoucoders.ecommerce.dtos.ClientOrderDto(c.id, c.totalPrice, c.clientId," +
            "cs.firstName, cs.lastName, cs.email, cs.phoneNumber, c.description, c.dateCreation," +
            " c.dateUpdate, c.clientOrderStatusId, os.name, os.color) " +
            "FROM ClientOrder c " +
            "LEFT JOIN  Client cs ON c.clientId = cs.id " +
            "LEFT JOIN  OrderStatus os ON c.clientOrderStatusId = os.id " +
            "WHERE (:orderId IS NULL OR c.id = :orderId) " +
            "AND (:orderStatusId IS NULL OR c.clientOrderStatusId = :orderStatusId) " +
            "AND (:clientId IS NULL OR c.clientId = :clientId) " +
            "AND (:dateCreation IS NULL OR c.dateCreation = :dateCreation) " +
            "AND (:dateUpdate IS NULL OR c.dateUpdate = :dateUpdate) ")
    List<ClientOrderDto> findAllClientOrders(
            @Param("orderId") Long orderId,
            @Param("clientId") Long clientId,
            @Param("dateCreation") LocalDateTime dateCreation,
            @Param("dateUpdate") LocalDateTime dateUpdate,
            @Param("orderStatusId") Long orderStatusId
    );
}
