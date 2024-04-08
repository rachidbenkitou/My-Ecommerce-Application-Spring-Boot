package com.benkitoucoders.ecommerce.quiz.dao;

import com.benkitoucoders.ecommerce.quiz.dto.CouponDto;
import com.benkitoucoders.ecommerce.quiz.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

;

@Repository
public interface CouponDao extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {
    @Query(value = "select new com.benkitoucoders.ecommerce.quiz.dto.CouponDto(" +
            "discountCoupon.id ,discountCoupon.code,discountCoupon.description,discountCoupon.discount," +
            "discountCoupon.discountCouponType,discountCoupon.active,discountCoupon.startDate,discountCoupon.endDate," +
            "discountCoupon.packageId,discountCoupon.productId,discountCoupon.dateCreation,discountCoupon.userCreation," +
            "discountCoupon.dateUpdate, discountCoupon.userUpdate,package.name,product.name) " +
            "from DiscountCoupon discountCoupon " +
            "left join Package  package on package.id=discountCoupon.packageId " +
            "left join Product  product on product.id=discountCoupon.productId "+
            " where (:id is null or discountCoupon.id=:id) " +
            " and (:code is null or LOWER(discountCoupon.code) like LOWER(CONCAT('%',:code,'%')))"+
            " and (:active is null or LOWER(discountCoupon.active) like LOWER(CONCAT('%',:active,'%')))"+
            " and (:productId is null or discountCoupon.productId=:productId) "+
            " and (:packageId is null or discountCoupon.packageId=:packageId) "
    )
    List<CouponDto> findByQuery(
            @Param("code") String  code,
            @Param("id") Long id,
            @Param("packageId") Long packageId ,
            @Param("productId") Long productId ,
            @Param("active") String  active
    );

}
