package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.dao.ImageDao;
import com.benkitoucoders.ecommerce.dao.ProductDao;
import com.benkitoucoders.ecommerce.dtos.ProductDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.exceptions.EntityAlreadyExistsException;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.exceptions.EntityServiceException;
import com.benkitoucoders.ecommerce.mappers.ProductMapper;
import com.benkitoucoders.ecommerce.services.inter.ImageService;
import com.benkitoucoders.ecommerce.services.inter.ProductService;
import com.benkitoucoders.ecommerce.services.strategy.inter.ImagesUploadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductMapper productMapper;
    private final ImageService imageService;
    private final ImagesUploadStrategy imagesUploadStrategy;
    private final ImageDao imageDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductMapper productMapper, ImageService imageService,
                              @Qualifier("productImageUploadStrategy") ImagesUploadStrategy imagesUploadStrategy, ImageDao imageDao) {
        this.productDao = productDao;
        this.productMapper = productMapper;
        this.imageService = imageService;
        this.imagesUploadStrategy = imagesUploadStrategy;
        this.imageDao = imageDao;
    }

    @Override
    @Cacheable(value = "productsByQueryCache", key = "{#id, #name, #price, #quantity, #visibility, #categoryId, #pageable}")
    public List<ProductDto> getProductsByQuery(Long id, String name, Double price, Integer quantity, String visibility, Long categoryId, Pageable pageable) {
        try {
            return productDao.getProductsByQuery(id, name, price, quantity, categoryId, visibility, pageable);
        } catch (Exception e) {
            throw new EntityServiceException("An error occurred while fetching the products.", e);
        }

    }

    @Override
    @Cacheable(value = "lastRecordedProductsCache", key = "'last15Recorded'")
    public List<ProductDto> getLastRecordedProductsByQuery() {
        try {
            return productDao.getLastRecordedProductsByQuery();

        } catch (Exception e) {
            throw new EntityServiceException("An error occurred while fetching 15 last record products.", e);
        }
        //It returns just last 15 recorded products
    }

    @Override
    @Cacheable(value = "topProductsCache", key = "'top15'")
    public List<ProductDto> getTop15MostOrderedProducts() {
        try {
            return productDao.getTop15MostOrderedProducts();
        } catch (Exception e) {
            throw new EntityServiceException("An error occurred while fetching top 15 most ordered products.", e);
        }
    }

    @Override
    @Cacheable(value = "productCache", key = "#id")
    public ProductDto getProductById(Long id) {
        try {
            List<ProductDto> productDtoList = productDao.getProductsByQuery(id, null, null, null, null, null, null);
            return productDtoList.stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(String.format("The product with the id %d is not found.", id)));
        } catch (Exception e) {
            throw new EntityServiceException("An error occurred while fetching product by id.", e);
        }

    }


    @Override
    @CacheEvict(value = {"productCache", "topProductsCache", "lastRecordedProductsCache", "productsByQueryCache"}, allEntries = true)
    public ProductDto addProduct(ProductDto productDto) throws IOException {
        try {
            if (productDao.existsByName(productDto.getName())) {
                throw new EntityAlreadyExistsException(String.format("The product with the name %s  is already exists", productDto.getName()));
            }
            productDto.setId(null);
            ProductDto savedProductDto = productMapper.modelToDto(productDao.save(productMapper.dtoToModel(productDto)));
            if (savedProductDto != null) {
                imagesUploadStrategy.uploadImages(productDto.getProductImages(), savedProductDto.getId());
                return savedProductDto;
            } else {
                throw new RuntimeException("Error while saving the product.");
            }
        } catch (Exception e) {
            throw new EntityServiceException("An error occurred while adding the product.", e);
        }

    }

    @Override
    @CacheEvict(value = {"productCache", "topProductsCache", "lastRecordedProductsCache", "productsByQueryCache"}, allEntries = true)
    public ProductDto updateProduct(Long id, ProductDto productDto) throws IOException {
        try {
            ProductDto oldProductDto = getProductById(id);
            productDto.setId(oldProductDto.getId());
            if (productDto.getProductImages() != null && !productDto.getProductImages().isEmpty()) {
                imageService.deleteImageByFilePathFromLocalSystem(oldProductDto.getFilePath());
                imageDao.deleteAllByProductId(oldProductDto.getId());
                imagesUploadStrategy.uploadImages(productDto.getProductImages(), productDto.getId());
            }
            return productMapper.modelToDto(productDao.save(productMapper.dtoToModel(productDto)));
        } catch (Exception e) {
            throw new EntityServiceException("An error occurred while updating the product.", e);
        }

    }

    @Override
    @CacheEvict(value = {"productCache", "topProductsCache", "lastRecordedProductsCache", "productsByQueryCache"}, allEntries = true)
    public ResponseDto deleteProductById(Long id) {
        try {
            ProductDto productToDelete = getProductById(id);
            productDao.deleteById(id);
            imageService.deleteImageByFilePathFromLocalSystem(productToDelete.getFilePath());
            return ResponseDto.builder()
                    .message("Product successfully deleted.")
                    .build();
        } catch (Exception e) {
            throw new EntityServiceException("An error occurred while deleting the product.", e);
        }
    }
}
