package com.benkitoucoders.ecommerce.dao;

import com.benkitoucoders.ecommerce.dtos.PackageProductDto;
import com.benkitoucoders.ecommerce.entities.PackageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackageProductRepository extends JpaRepository<PackageProduct, Long>, JpaSpecificationExecutor<PackageProduct> {
    @Query(value = "select new com.benkitoucoders.ecommerce.dtos.PackageProductDto(" +
            " packageProduct.id,packageProduct.packageId,packageProduct.productId,packageProduct.dateCreation," +
            "packageProduct.userCreation,packageProduct.dateUpdate,packageProduct.userUpdate)" +
            " from PackageProduct packageProduct " +
            "where (:id is null or packageProduct.id=:id ) " +
            "and (:productId is null or packageProduct.productId=:productId )" +
            "and (:packageId is null or packageProduct.packageId=:packageId )"
    )
    public List<PackageProductDto> getAllServiceOptionByQuery(
            @Param("id") Long id,
            @Param("packageId") Long packageId,
            @Param("productId") Long serviceOptionId
    );

    List<PackageProduct> findAllByPackageId(Long packageId);
}
