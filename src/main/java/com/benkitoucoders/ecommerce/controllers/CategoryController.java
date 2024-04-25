
package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.dtos.CategoryDto;
import com.benkitoucoders.ecommerce.services.inter.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategoriesByQuery(@RequestParam(name = "categoryId", required = false) Long categoryId,
                                                                  @RequestParam(name = "categoryName", required = false) String categoryName,
                                                                  @RequestParam(name = "categoryVisbility", required = false) String categoryVisbility
    ) {
        return ResponseEntity.ok().body(categoryService.getCategoriesByQuery(categoryId, categoryName, categoryVisbility));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok().body(categoryService.getCategoryById(categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(
            @RequestPart(name = "categoryDto") String categoryDtoJson
            , @RequestPart(name = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryDto categoryDto = null;
        try {
            // Convert JSON string to CategoryDto
            categoryDto = objectMapper.readValue(categoryDtoJson, CategoryDto.class);
            categoryDto.setCategoryImage(image);
        } catch (IOException e) {
            throw new RuntimeException("Error while transforming productDtoJson to categoryDto Object.");
        }
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }


    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long categoryId
            , @RequestPart(name = "categoryDto") String categoryDtoJson
            , @RequestPart(name = "image", required = false) MultipartFile image) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryDto categoryDto = null;
        try {
            // Convert JSON string to CategoryDto
            categoryDto = objectMapper.readValue(categoryDtoJson, CategoryDto.class);
            categoryDto.setCategoryImage(image);
        } catch (IOException e) {
            throw new RuntimeException("Error while transforming productDtoJson to categoryDto Object.");
        }
        return ResponseEntity.ok().body(categoryService.updateCategory(categoryId, categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long categoryId) {

        return ResponseEntity.ok().body(categoryService.deleteCategoryById(categoryId));
    }


}


