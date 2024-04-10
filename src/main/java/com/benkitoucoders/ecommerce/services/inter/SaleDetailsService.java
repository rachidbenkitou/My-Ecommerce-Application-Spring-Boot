package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.criteria.SaleDetailsCriteria;
import com.benkitoucoders.ecommerce.dtos.SaleDetailsDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;

import java.util.List;

public interface SaleDetailsService {
    List<SaleDetailsDto> findSaleDetailsByCriteria(SaleDetailsCriteria saleDetailsCriteria) throws EntityNotFoundException;

    SaleDetailsDto findSaleDetailsById(Long id) throws EntityNotFoundException;

    List<SaleDetailsDto> findAllSaleDetailsById(Long id) throws EntityNotFoundException;

    SaleDetailsDto persistSaleDetails(SaleDetailsDto saleDetailsDto) throws EntityNotFoundException;

    SaleDetailsDto updatesaleDetails(Long id, SaleDetailsDto saleDetailsDto) throws EntityNotFoundException;

    ResponseDto deleteSaleDetailsById(Long id) throws EntityNotFoundException;
}
