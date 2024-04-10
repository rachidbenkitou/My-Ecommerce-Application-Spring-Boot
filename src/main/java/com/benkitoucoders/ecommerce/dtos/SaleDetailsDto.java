package com.benkitoucoders.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SaleDetailsDto {

    private Long id;
    private Long saleId;
    private Long productId;
    private Long packageId;
    private Long packageQuantity;
    private LocalDateTime dateCreation;
    private LocalDateTime dateUpdate;
    private String productName;
    private SaleDto saleDto;
    private PackageDto packageDto;
    private Double price;
    private Integer quantity;

    public SaleDetailsDto(Long id,
                          Long productId, String productName, Integer quantity, Double price, Long saleId, LocalDateTime dateCreation, LocalDateTime dateUpdate) {
        this.id = id;
        this.dateCreation = dateCreation;
        this.dateUpdate = dateUpdate;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.saleId = saleId;
        this.productName = productName;
    }
}
