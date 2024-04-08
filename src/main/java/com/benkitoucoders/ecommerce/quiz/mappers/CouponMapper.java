package com.benkitoucoders.ecommerce.quiz.mappers;

import com.benkitoucoders.ecommerce.quiz.dto.CouponDto;
import com.benkitoucoders.ecommerce.quiz.entity.Coupon;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@Component
public interface CouponMapper {
    CouponDto modelToDto(Coupon source);

    List<CouponDto> modelsToDtos(List<Coupon> sourceList);

    Coupon dtoToModel(CouponDto couponDto);
}
