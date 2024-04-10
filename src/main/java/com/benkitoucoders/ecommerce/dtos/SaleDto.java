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
public class SaleDto {

    private Long id;
    private LocalDateTime dateCreation;
    private LocalDateTime dateUpdate;
    private String address;
    private String fullName;
    private String phone;
    private String email;
    private Double totalPrice;
    private Long saleStatusId;
    private String saleStatusName;
    private String saleStatusColor;

    public SaleDto(Long id, String address, String email, String phone, Double totalPrice, String saleStatus,
                   LocalDateTime dateCreation, LocalDateTime dateUpdate, Long saleStatusId,
                   String saleStatusName, String saleStatusColor
    ) {
        this.id = id;
        this.dateCreation = dateCreation;
        this.dateUpdate = dateUpdate;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.saleStatusId = saleStatusId;
        this.saleStatusName = saleStatusName;
        this.saleStatusColor = saleStatusColor;
    }


}
