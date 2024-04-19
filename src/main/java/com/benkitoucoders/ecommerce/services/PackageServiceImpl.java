package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.criteria.PackageCriteria;
import com.benkitoucoders.ecommerce.criteria.PackageProductCriteria;
import com.benkitoucoders.ecommerce.dao.ImageDao;
import com.benkitoucoders.ecommerce.dao.PackageRepository;
import com.benkitoucoders.ecommerce.dtos.PackageDto;
import com.benkitoucoders.ecommerce.dtos.PackageProductDto;
import com.benkitoucoders.ecommerce.dtos.ProductDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.exceptions.EntityServiceException;
import com.benkitoucoders.ecommerce.mappers.PackageMapper;
import com.benkitoucoders.ecommerce.services.inter.ImageService;
import com.benkitoucoders.ecommerce.services.inter.PackageProductService;
import com.benkitoucoders.ecommerce.services.inter.PackageService;
import com.benkitoucoders.ecommerce.services.strategy.inter.ImagesUploadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;
    private final PackageProductService packageProductService;
    private final ProductServiceImpl productService;
    private final ImageService imageService;
    private final ImagesUploadStrategy imagesUploadStrategy;
    private final ImageDao imageDao;

    @Autowired
    public PackageServiceImpl(
            PackageRepository packageRepository,
            PackageMapper packageMapper,
            PackageProductService packageProductService,
            ProductServiceImpl productService,
            ImageService imageService,
            @Qualifier("packageImageUploadStrategy") ImagesUploadStrategy imagesUploadStrategy,
            ImageDao imageDao
    ) {
        this.packageRepository = packageRepository;
        this.packageMapper = packageMapper;
        this.packageProductService = packageProductService;
        this.productService = productService;
        this.imageService = imageService;
        this.imagesUploadStrategy = imagesUploadStrategy;
        this.imageDao = imageDao;
    }


    public List<PackageDto> findPackagesByCriteria(PackageCriteria packageCriteria, Pageable pageable) {
        List<PackageDto> packageDtosList = packageRepository.getAllPackageByQuery(packageCriteria.getId(), packageCriteria.getName(), packageCriteria.getActive(), pageable);
        PackageProductCriteria packageProductCriteria = new PackageProductCriteria();
        List<PackageProductDto> packageProductDtoList = new ArrayList<>();
        ProductDto productDto1 = null;

        if (packageDtosList.isEmpty())
            return new ArrayList<>();

        for (PackageDto packageDto : packageDtosList) {

            packageProductCriteria.setPackageId(packageDto.getId());
            packageProductDtoList = packageProductService.findPackageProductByCriteria(packageProductCriteria);
            for (PackageProductDto packageProductDto : packageProductDtoList) {
                productDto1 = productService.getProductById(packageProductDto.getProductId());
                packageDto.getProductDtos().add(productDto1);
            }
        }
        return packageDtosList;
    }


    public PackageDto findPackagesById(Long id) throws EntityNotFoundException {
        PackageCriteria packageCriteria = PackageCriteria.builder().id(id).build();
        List<PackageDto> packageDtosList = packageRepository.getAllPackageByQuery(
                packageCriteria.getId(), packageCriteria.getName(), packageCriteria.getActive(), null);
        if (packageDtosList.isEmpty()) {
            throw new EntityNotFoundException("The package does not exists. ");
        }
        List<PackageProductDto> packageProductDtoList = packageProductService.findPackageProductByPackageId(packageDtosList.get(0).getId());
        packageDtosList.get(0).setProductDtos(new ArrayList<>());
        for (PackageProductDto packageProductDto : packageProductDtoList) {
            ProductDto productDto = productService.getProductById(packageProductDto.getProductId());
            packageDtosList.get(0).getProductDtos().add(productDto);
        }
        return packageDtosList.get(0);
    }

    public PackageDto persistPackages(PackageDto packagesDto) throws EntityNotFoundException, IOException {
        PackageDto savedPackageDto = packageMapper.modelToDto(packageRepository.save(packageMapper.dtoToModel(packagesDto)));

        if (savedPackageDto != null) {
            imagesUploadStrategy.uploadImages(packagesDto.getPackageImages(), savedPackageDto.getId());
            return savedPackageDto;
        } else {
            throw new RuntimeException("Error while saving the package.");
        }
    }

    public PackageDto updatePackages(Long id, PackageDto packagesDto) throws EntityNotFoundException {
        try {
            PackageDto oldPackageDto = findPackagesById(id);
            packagesDto.setId(oldPackageDto.getId());
            if (packagesDto.getPackageImages() != null && !packagesDto.getPackageImages().isEmpty()) {
                imageService.deleteImageByFilePathFromLocalSystem(oldPackageDto.getFilePath());
                imageDao.deleteAllByPackageId(oldPackageDto.getId());
                imagesUploadStrategy.uploadImages(packagesDto.getPackageImages(), packagesDto.getId());
            }
            return packageMapper.modelToDto(packageRepository.save(packageMapper.dtoToModel(packagesDto)));
        } catch (Exception e) {
            throw new EntityServiceException("An error occurred while updating the package.", e);
        }
    }

    public ResponseDto deletePackagesById(Long id) throws EntityNotFoundException {
        ResponseDto responseDto = new ResponseDto();
        PackageDto packagesDto = findPackagesById(id);
        // verifier si packege deja vendu
        packageProductService.deleteProductFromPackage(packagesDto.getId());
        packageRepository.deleteById(id);
        imageService.deleteImageByFilePathFromLocalSystem(packagesDto.getFilePath());

        responseDto.setMessage("élément bien supprimé");
        return responseDto;
    }

}
