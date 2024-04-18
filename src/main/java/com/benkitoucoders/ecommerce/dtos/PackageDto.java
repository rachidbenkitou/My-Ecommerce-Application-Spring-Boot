package com.benkitoucoders.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDto {
    private Long id;
    private String name;
    private String description;
    private String active;
    private String packageDetails;
    private LocalDateTime dateCreation;
    private Long userCreation;
    private LocalDateTime dateUpdate;
    private Long userUpdate;
    private Double price;
    private Double comparePrice;
    private String packageImagePath;


    private List<ProductDto> productDtos;


    public PackageDto(Long id, String name, String description, LocalDateTime dateCreation, Long userCreation,
                      LocalDateTime dateUpdate, Long userUpdate, Double price, String active, String packageDetails, Double comparePrice, String packageImagePath) {
        this.id = id;
        this.name = name;
        this.description = description;

        this.active = active;
        this.dateCreation = dateCreation;
        this.userCreation = userCreation;
        this.dateUpdate = dateUpdate;
        this.userUpdate = userUpdate;
        this.price = price;
        this.comparePrice = comparePrice;
        this.packageImagePath = packageImagePath;
        this.packageDetails = packageDetails;
    }
}
