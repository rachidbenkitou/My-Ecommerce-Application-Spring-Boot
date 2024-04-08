package com.benkitoucoders.ecommerce.services.strategy.quiz;
import com.benkitoucoders.ecommerce.quiz.dao.CouponDao;
import com.benkitoucoders.ecommerce.quiz.dto.CouponDto;
import com.benkitoucoders.ecommerce.quiz.entity.Coupon;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.quiz.mappers.CouponMapper;
import com.benkitoucoders.ecommerce.services.ProductServiceImpl;
import com.benkitoucoders.ecommerce.criteria.DiscountCouponCriteria;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.quiz.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponServiceImp implements CouponService {

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private ProductServiceImpl productService;

    public List<CouponDto> findDiscountCouponsByCriteria(DiscountCouponCriteria DiscountCouponCriteria) throws EntityNotFoundException {
        return couponDao.findByQuery(
                DiscountCouponCriteria.getCode(),DiscountCouponCriteria.getId(),
                DiscountCouponCriteria.getPackageId(),DiscountCouponCriteria.getProductId(),
                DiscountCouponCriteria.getActive());
    }

    public CouponDto findDiscountCouponsById(Long id) throws EntityNotFoundException
    {
        DiscountCouponCriteria DiscountCouponCriteria = new DiscountCouponCriteria();
        DiscountCouponCriteria.setId(id);
        List<CouponDto> DiscountCouponDtoList = findDiscountCouponsByCriteria(DiscountCouponCriteria);
        if (DiscountCouponDtoList != null && !DiscountCouponDtoList.isEmpty()) {
            return DiscountCouponDtoList.get(0);
        } else {
            throw new EntityNotFoundException("The coupon with the id "+id+ "  is not found.");
        }
    }


    public CouponDto persistDiscountCoupons(CouponDto discountCouponDto) throws EntityNotFoundException {
        Coupon save = couponMapper.dtoToModel(discountCouponDto);

        return  couponMapper.modelToDto(couponDao.save(save));
    }

    public CouponDto updateDiscountCoupons(Long id, CouponDto discountCouponDto) throws EntityNotFoundException  {

        CouponDto discountCouponDto1 = findDiscountCouponsById(id);
        discountCouponDto1.setId(id);
        discountCouponDto1.setDateUpdate(LocalDateTime.now());
        return       couponMapper.modelToDto(couponDao.save(couponMapper.dtoToModel(discountCouponDto1)));
    }

    public ResponseDto deleteDiscountCouponsById(Long id) throws EntityNotFoundException {
        ResponseDto responseDto = new ResponseDto();
        CouponDto DiscountCouponDto = findDiscountCouponsById(id);
        //Product productCriteria=productCriteria.builder().discountCouponId(DiscountCouponDto.getId()).build();
        // List<productDto> productDtoList=productService.getProductsByQuery(productCriteria);
        // if(!productDtoList.isEmpty()){
        //  throw new EntityNotFoundException("cette réduction encore d'utilisation");
        //}
        couponDao.deleteById(id);
        responseDto.setMessage("élément bien supprimé");
        return responseDto;
    }

}
