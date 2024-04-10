package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.dao.SupplierProductDao;
import com.benkitoucoders.ecommerce.dtos.ProductDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.dtos.SupplierProductDto;
import com.benkitoucoders.ecommerce.entities.SupplierProduct;
import com.benkitoucoders.ecommerce.mappers.SupplierProductMapper;
import com.benkitoucoders.ecommerce.services.inter.ProductService;
import com.benkitoucoders.ecommerce.services.inter.SupplierProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SupplierProductServiceImpl implements SupplierProductService {

    private final SupplierProductDao supplierProductDao;
    private final SupplierProductMapper supplierProductMapper;
    private final ProductService productService;

    /**
     * Retrieves all supplier products.
     *
     * @return a list of {@link SupplierProductDto}
     */
    @Override
    public List<SupplierProductDto> getAllSupplierProduct() {
        log.info("fetching all supplier products");
        List<SupplierProduct> supplierProducts = supplierProductDao.findAll();
        return supplierProductMapper.modelsToDtos(supplierProducts);
    }

    /**
     * Retrieves a supplier product by its ID.
     *
     * @param id the ID of the supplier product
     * @return the {@link SupplierProductDto} or null if not found
     */
    @Override
    public SupplierProductDto getSupplierProductById(Long id) {
        log.info("fetxhing supplier product with id : {}", id);
        Optional<SupplierProduct> supplierProduct = supplierProductDao.findById(id);
        return supplierProduct.map(supplierProductMapper::modelToDto).orElse(null);
    }

    /**
     * Retrieves a supplier product by supplier ID.
     *
     * @param supplierId the ID of the supplier
     * @return the {@link SupplierProductDto} or null if not found
     */
    @Override
    public SupplierProductDto getSupplierProductBySupplierId(Long supplierId) {
        log.info("fetching supplier product for supplier id:{}", supplierId);
        List<SupplierProduct> supplierProducts = supplierProductDao.findBySupplierId(supplierId);
        return supplierProducts.stream().findFirst().map(supplierProductMapper::modelToDto).orElse(null);
    }

    /**
     * Adds a new supplier product.
     *
     * @param supplierProductDto the DTO of the supplier product to add
     * @return the added {@link SupplierProductDto}
     * @throws IOException if an I/O error occurs
     */
    @Override
    public SupplierProductDto addSupplierProduct(SupplierProductDto supplierProductDto) throws IOException {
        log.info("Adding new supplier product");

        // Convert DTO to model and save supplier product
        SupplierProduct supplierProduct = supplierProductMapper.dtoToModel(supplierProductDto);
        SupplierProduct savedSupplierProduct = supplierProductDao.save(supplierProduct);
        log.info("Supplier product added: {}", savedSupplierProduct);

        // Fetch product information
        ProductDto productDto = productService.getProductById(supplierProductDto.getProductId());

        // Update product quantity
        int newQuantity = productDto.getQuantity() + supplierProductDto.getQuantity();
        productDto.setQuantity(newQuantity);
        productService.updateProduct(productDto.getId(), productDto);
        log.info("Product quantity updated for product ID {}: New Quantity = {}", productDto.getId(), newQuantity);

        return supplierProductMapper.modelToDto(savedSupplierProduct);
    }


    /**
     * Updates an existing supplier product.
     *
     * @param id                 the ID of the supplier product to update
     * @param supplierProductDto the DTO of the supplier product with updated information
     * @return the updated {@link SupplierProductDto} or null if not found
     */
    @Override
    public SupplierProductDto updateSupplierProduct(Long id, SupplierProductDto supplierProductDto) {
        log.info("Updating supplier product with ID: {}", id);
        Optional<SupplierProduct> existingSupplierProduct = supplierProductDao.findById(id);
        if (existingSupplierProduct.isPresent()) {
            SupplierProduct updatedSupplierProduct = existingSupplierProduct.get();
            updatedSupplierProduct.setPrice(supplierProductDto.getPrice());
            updatedSupplierProduct.setQuantity(supplierProductDto.getQuantity());
            supplierProductDao.save(updatedSupplierProduct);
            return supplierProductMapper.modelToDto(updatedSupplierProduct);
        } else {
            log.warn("Supplier product with ID: {} not found", id);
            return null;
        }
    }

    /**
     * Deletes a supplier product by its ID.
     *
     * @param id the ID of the supplier product to delete
     * @return a {@link ResponseDto} indicating the outcome
     */
    @Override
    public ResponseDto deleteSupplierProductById(Long id) {
        log.info("Deleting supplier product with ID: {}", id);
        supplierProductDao.deleteById(id);
        return new ResponseDto("Supplier product successfully deleted", true);
    }
}
