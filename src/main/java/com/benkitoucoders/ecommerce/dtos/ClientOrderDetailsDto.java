package com.benkitoucoders.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientOrderDetailsDto implements Serializable {
    private Long id;
    private Long clientOrderId;
    private Long productId;
    private Long packageId;
    private String productName;
    private Double price;
    private Integer quantity;

    ClientOrderDetailsDto(Long id, Long clientOrderId, Long productId, String productName, Double price, Integer quantity, Long packageId) {
        this.id = id;
        this.clientOrderId = clientOrderId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.packageId = packageId;
    }
}
