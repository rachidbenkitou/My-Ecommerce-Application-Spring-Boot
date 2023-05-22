package com.benkitoucoders.myecommerce.dtos;

import com.benkitoucoders.myecommerce.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SubCategoryRequestDto implements Serializable {
    @NotBlank(message = "The subCategory name should not be null or empty")
    @Size(min = 1, max = 20, message = "The subCategory name must be between 1 and 20 characters")
    private String name;

    @NotNull(message = "The category is required")
    private Category category;
}
