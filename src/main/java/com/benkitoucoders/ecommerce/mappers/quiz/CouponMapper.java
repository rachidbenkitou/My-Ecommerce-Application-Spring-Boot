package com.benkitoucoders.ecommerce.mappers.quiz;

import com.benkitoucoders.ecommerce.dtos.quiz.CouponDto;
import com.benkitoucoders.ecommerce.entities.quiz.Coupon;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@Component
public interface CouponMapper {
    CouponDto modelToDto(Coupon coupon);

    List<CouponDto> modelsToDtos(List<Coupon> couponList);

    Coupon dtoToModel(CouponDto couponDto);


}
