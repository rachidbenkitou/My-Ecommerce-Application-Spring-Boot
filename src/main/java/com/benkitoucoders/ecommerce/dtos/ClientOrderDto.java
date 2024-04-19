package com.benkitoucoders.ecommerce.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ClientOrderDto implements Serializable {
    private Long id;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime dateUpdate;
    private Double totalPrice;
    private Long clientId;
    private String clientFirstName;
    private String clientLastName;
    private String clientEmail;
    private String clientPhoneNumber;
    private Long clientOrderStatusId;
    private String clientOrderStatusName;
    private String clientOrderStatusColor;
    private List<ClientOrderDetailsDto> clientOrderDetailsDtos = new ArrayList<>();

    public ClientOrderDto(Long id, Double totalPrice, Long clientId, String clientFirstName, String clientLastName,
                          String clientEmail, String clientPhoneNumber, String description,
                          LocalDateTime dateCreation, LocalDateTime dateUpdate,
                          Long clientOrderStatusId, String clientOrderStatusName, String clientOrderStatusColor) {

        this.id = id;
        this.totalPrice = totalPrice;
        this.clientId = clientId;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientEmail = clientEmail;
        this.clientPhoneNumber = clientPhoneNumber;
        this.description = description;
        this.dateCreation = dateCreation;
        this.dateUpdate = dateUpdate;
        this.clientOrderStatusId = clientOrderStatusId;
        this.clientOrderStatusName = clientOrderStatusName;
        this.clientOrderStatusColor = clientOrderStatusColor;
    }
}
