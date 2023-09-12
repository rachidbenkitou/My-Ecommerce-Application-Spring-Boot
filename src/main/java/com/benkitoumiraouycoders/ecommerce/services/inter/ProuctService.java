package com.benkitoumiraouycoders.ecommerce.services.inter;

import com.benkitoumiraouycoders.ecommerce.dtos.ProductDto;
import com.benkitoumiraouycoders.ecommerce.handlers.ResponseDto;

import java.util.List;

public interface ProuctService {

    List<ProductDto> getProductsByQuery(Long id, String name, Double price, Integer quantity, String visbility, Long categoryId);

    ProductDto getProductById(Long id);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    ResponseDto deleteProductById(Long id);
}