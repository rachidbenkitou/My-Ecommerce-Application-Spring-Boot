package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.dtos.ProductDto;
import com.benkitoucoders.ecommerce.exceptions.EntityAlreadyExistsException;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    /**
     * Retrieves a list of products based on the provided search criteria.
     * <p>
     * The results are cached to improve performance for subsequent requests with identical parameters.
     *
     * @param id         The product ID. Can be {@code null} to ignore this filter.
     * @param name       The name of the product. Can be {@code null} to ignore this filter.
     * @param price      The price of the product. Can be {@code null} to ignore this filter.
     * @param quantity   The quantity of the product. Can be {@code null} to ignore this filter.
     * @param visibility The visibility status of the product. Can be {@code null} to ignore this filter.
     * @param categoryId The category ID of the product. Can be {@code null} to ignore this filter.
     * @param pageable   Pagination information.
     * @return A list of {@link ProductDto} matching the search criteria.
     */
    List<ProductDto> getProductsByQuery(Long id, String name, Double price, Integer quantity, String visibility, Long categoryId, Pageable pageable);

    /**
     * Retrieves the last 15 recorded products.
     * <p>
     * Results are cached to reduce database load for this frequently accessed data.
     *
     * @return A list of the last 15 recorded {@link ProductDto}.
     */
    List<ProductDto> getLastRecordedProductsByQuery();

    /**
     * Retrieves the top 15 most ordered products.
     * <p>
     * This data is cached to provide quick access to this frequently requested information.
     *
     * @return A list of {@link ProductDto} representing the top 15 most ordered products.
     */
    List<ProductDto> getTop15MostOrderedProducts();

    /**
     * Retrieves a product by its ID.
     * <p>
     * The result is cached to optimize performance for frequently requested products.
     *
     * @param id The ID of the product to retrieve.
     * @return The {@link ProductDto} of the requested product.
     * @throws EntityNotFoundException If the product with the specified ID is not found.
     */
    ProductDto getProductById(Long id) throws EntityNotFoundException;

    /**
     * Adds a new product.
     * <p>
     * This method evicts all entries from the caches to ensure the freshness of data
     * after a new product is added.
     *
     * @param productDto The {@link ProductDto} to add.
     * @return The saved {@link ProductDto} with its new ID.
     * @throws IOException                  If an error occurs during image upload.
     * @throws EntityAlreadyExistsException If a product with the same name already exists.
     */
    ProductDto addProduct(ProductDto productDto) throws IOException, EntityAlreadyExistsException;

    /**
     * Updates an existing product.
     * <p>
     * This method evicts all entries from the caches to ensure data consistency after a product update.
     *
     * @param id         The ID of the product to update.
     * @param productDto The updated product data.
     * @return The updated {@link ProductDto}.
     * @throws IOException If an error occurs during image upload.
     */
    ProductDto updateProduct(Long id, ProductDto productDto) throws IOException;

    /**
     * Deletes a product by its ID.
     * <p>
     * This method evicts all entries from the caches to ensure data consistency after a product is deleted.
     * It also handles the deletion of associated images to maintain data integrity.
     *
     * @param id The ID of the product to delete.
     * @return A {@link ResponseDto} indicating the outcome of the operation.
     */
    ResponseDto deleteProductById(Long id);
}
