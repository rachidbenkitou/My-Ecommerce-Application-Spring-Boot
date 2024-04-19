package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.criteria.PackageProductCriteria;
import com.benkitoucoders.ecommerce.dtos.PackageProductDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;

import java.util.List;

public interface PackageProductService {
    public ResponseDto deleteProductFromPackage(Long packageId) throws EntityNotFoundException;
    public ResponseDto deletePackageProductById(Long id) throws EntityNotFoundException;
    List<PackageProductDto> findPackageProductByPackageId(Long packageId) throws EntityNotFoundException;
    public PackageProductDto updatePackageProduct(Long id, PackageProductDto PackageProductDto) throws EntityNotFoundException;
    public PackageProductDto persistPackageProduct(PackageProductDto PackageProductDto) throws EntityNotFoundException ;
    public PackageProductDto findPackageProductById(Long id) throws EntityNotFoundException ;
    public List<PackageProductDto> findPackageProductByCriteria(PackageProductCriteria PackageProductCriteria) throws EntityNotFoundException ;

    }
