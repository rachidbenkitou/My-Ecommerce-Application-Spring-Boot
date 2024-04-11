package com.benkitoucoders.ecommerce.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
//@NoArgsConstructor
public class CategoryDto implements Serializable {

    CategoryDto() {
    }

    private Long id;

    private String name;

    private String visbility;
    private Integer categoryOrder;


    private String categoryImagePath;

    private String categoryImageUrl;
    private String filePath;
    private MultipartFile categoryImage;

    public CategoryDto(Long id, String name, String visibility, String categoryImagePath, String filePath,  Integer categoryOrder) {
        this.id = id;
        this.name = name;
        this.visbility = visibility;
        this.categoryImagePath = categoryImagePath;
        this.filePath=filePath;
        this.categoryOrder=categoryOrder;
    }

}
