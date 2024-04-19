package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.criteria.PackageProductCriteria;
import com.benkitoucoders.ecommerce.dao.PackageProductRepository;
import com.benkitoucoders.ecommerce.dtos.PackageProductDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.mappers.PackageProductMapper;
import com.benkitoucoders.ecommerce.services.inter.PackageProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PackageProductServiceImpl implements PackageProductService {

    @Autowired
    private PackageProductRepository packageProductRepository;

    @Autowired
    private PackageProductMapper packageProductMapper;


    public List<PackageProductDto> findPackageProductByCriteria(PackageProductCriteria PackageProductCriteria) throws EntityNotFoundException {

        try {
            return packageProductRepository
                    .getAllServiceOptionByQuery(
                            PackageProductCriteria.getId(),
                            PackageProductCriteria.getPackageId(),
                            PackageProductCriteria.getProductId()
                    );

        } catch (Exception e) {
            throw new EntityNotFoundException("Error while executing this method (findProductByQuery)");
        }
    }

    public PackageProductDto findPackageProductById(Long id) throws EntityNotFoundException {
        PackageProductCriteria PackageProductCriteria = new PackageProductCriteria();
        PackageProductCriteria.setId(id);
        List<PackageProductDto> PackageProductDtoList = findPackageProductByCriteria(PackageProductCriteria);
        if (PackageProductDtoList != null && !PackageProductDtoList.isEmpty()) {
            return PackageProductDtoList.get(0);
        } else {
            throw new EntityNotFoundException("�l�ment n'existe pas");
        }
    }

    public List<PackageProductDto> findPackageProductByPackageId(Long packageId) throws EntityNotFoundException {
        PackageProductCriteria PackageProductCriteria = new PackageProductCriteria();
        PackageProductCriteria.setPackageId(packageId);
        List<PackageProductDto> packageProductDtoList = findPackageProductByCriteria(PackageProductCriteria);
        return packageProductDtoList;
    }

    public PackageProductDto persistPackageProduct(PackageProductDto PackageProductDto) throws EntityNotFoundException {
        PackageProductDto.setDateCreation(LocalDateTime.now());
        return packageProductMapper.modelToDto(packageProductRepository.save(packageProductMapper.dtoToModel(PackageProductDto)));
    }

    public PackageProductDto updatePackageProduct(Long id, PackageProductDto PackageProductDto) throws EntityNotFoundException {

        PackageProductDto PackageProductDto1 = findPackageProductById(id);
        PackageProductDto.setId(id);
        PackageProductDto.setDateUpdate(LocalDateTime.now());
        return packageProductMapper.modelToDto(packageProductRepository.save(packageProductMapper.dtoToModel(
                PackageProductDto)));
    }

    public ResponseDto deletePackageProductById(Long id) throws EntityNotFoundException {
        ResponseDto responseDto = new ResponseDto();
        PackageProductDto PackageProductDto = findPackageProductById(id);
        packageProductRepository.deleteById(id);
        responseDto.setMessage("élément bien supprim�");
        return responseDto;
    }

    public ResponseDto deleteProductFromPackage(Long packageId) throws EntityNotFoundException {
        ResponseDto responseDto = new ResponseDto();
        PackageProductCriteria packageProductCriteria = PackageProductCriteria.builder().packageId(packageId).build();
        List<PackageProductDto> findPackageProductByCriteria = findPackageProductByCriteria(packageProductCriteria);
        findPackageProductByCriteria.forEach(packageProductDto -> {
            packageProductRepository.deleteById(packageProductDto.getId());
        });
        responseDto.setMessage("les éléments  bien supprimés");
        return responseDto;
    }


}
