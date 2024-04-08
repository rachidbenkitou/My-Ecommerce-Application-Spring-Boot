package com.benkitoucoders.ecommerce.services.inter.quiz;

import com.benkitoucoders.ecommerce.criteria.DiscountCouponCriteria;
import com.benkitoucoders.ecommerce.dtos.quiz.CouponDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;

import java.util.List;

public interface CouponService {
    public ResponseDto deleteDiscountCouponsById(Long id) throws EntityNotFoundException;
    public CouponDto updateDiscountCoupons(Long id, CouponDto discountCouponDto) throws EntityNotFoundException  ;
    public CouponDto persistDiscountCoupons(CouponDto discountCouponDto) throws EntityNotFoundException ;
    public CouponDto findDiscountCouponsById(Long id) throws EntityNotFoundException;
    public List<CouponDto> findDiscountCouponsByCriteria(DiscountCouponCriteria DiscountCouponCriteria) throws EntityNotFoundException ;
}