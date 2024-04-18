package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.criteria.PackageCriteria;
import com.benkitoucoders.ecommerce.dtos.PackageDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PackageService {
    public List<PackageDto> findPackagesByCriteria(PackageCriteria packageCriteria, Pageable pageable);

    public PackageDto findPackagesById(Long id) throws EntityNotFoundException;

    public PackageDto persistPackages(PackageDto packagesDto) throws EntityNotFoundException;

    public PackageDto updatePackages(Long id, PackageDto packagesDto) throws EntityNotFoundException;

    public ResponseDto deletePackagesById(Long id) throws EntityNotFoundException;
}
