package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.dtos.ProductDto;
import com.benkitoucoders.ecommerce.services.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;


    @GetMapping
//    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<List<ProductDto>> getProductsByQuery(@RequestParam(name = "productId", required = false) Long id,
                                                               @RequestParam(name = "productName", required = false) String name,
                                                               @RequestParam(name = "productPrice", required = false) Double price,
                                                               @RequestParam(name = "productQuantity", required = false) Integer quantity,
                                                               @RequestParam(name = "productVisibility", required = false) String productVisibility,
                                                               @RequestParam(name = "categoryId", required = false) Long categoryId,
                                                               @RequestParam(defaultValue = "0") int page, // Default page number
                                                               @RequestParam(defaultValue = "10") int size, // Default page size
                                                               @RequestParam(defaultValue = "id") String sortProperty, // Sort property
                                                               @RequestParam(defaultValue = "ASC") String sortDirection,
                                                               HttpServletRequest request) {


        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortProperty);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok().body(productService.getProductsByQuery(id, name, price, quantity, productVisibility, categoryId, pageable));
    }

    @GetMapping("/lastRecorded")
    public ResponseEntity<List<ProductDto>> getLastRecordedProductsByQuery() {

        return ResponseEntity.ok().body(productService.getLastRecordedProductsByQuery());
    }

    @GetMapping("/mostOrdered")
    public ResponseEntity<List<ProductDto>> getTop15MostOrderedProducts() {

        return ResponseEntity.ok().body(productService.getLastRecordedProductsByQuery());
    }

    @GetMapping("/{productId}")
//    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId
    ) {

        return ResponseEntity.ok().body(productService.getProductById(productId));
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(
            @RequestPart(name = "productDto") String productDtoJson
            , @RequestPart(name = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto productDto = null;
        try {
            // Convert JSON string to ProductDto
            productDto = objectMapper.readValue(productDtoJson, ProductDto.class);
            productDto.setProductImages(images);
        } catch (IOException e) {
            throw new RuntimeException("Error while transforming productDtoJson to productDto Object.");
        }
        ProductDto savedProduct = productService.addProduct(productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);

    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long productId
            , @RequestPart(name = "productDto") String productDtoJson
            , @RequestPart(name = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto productDto = null;
        try {
            // Convert JSON string to ProductDto
            productDto = objectMapper.readValue(productDtoJson, ProductDto.class);
            productDto.setProductImages(images);
        } catch (IOException e) {
            throw new RuntimeException("Error while transforming productDtoJson to productDto Object.");
        }
        return ResponseEntity.ok().body(productService.updateProduct(productId, productDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long productId) {

        return ResponseEntity.ok().body(productService.deleteProductById(productId));
    }

}
