package com.benkitoucoders.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Double comparePrice;

    private Integer quantity;

    private String visibility;
    private Integer productOrder;


    private Long categoryId;

    private String categoryName;

    private LocalDate dateCreated;

    private LocalDate dateUpdated;

    private String productImagePath;
    private String filePath;
    private String productPrincipalImageUrl;
    private List<MultipartFile> productImages;

    public ProductDto(Long id, String name, String description, Double price
            , Double comparePrice, Integer quantity, String visibility, Long categoryId
            , String categoryName, LocalDate dateCreated, LocalDate dateUpdated
            , String productImagePath, String filePath, Integer productOrder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.comparePrice = comparePrice;
        this.quantity = quantity;
        this.visibility = visibility;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.productImagePath = productImagePath;
        this.filePath = filePath;
        this.productOrder = productOrder;
    }
}
