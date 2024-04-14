package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.criteria.SaleCriteria;
import com.benkitoucoders.ecommerce.dtos.SaleDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;

import java.io.IOException;
import java.util.List;

public interface SaleService {
    public List<SaleDto> findsalesByCriteria(SaleCriteria saleCriteria) throws EntityNotFoundException ;
    public SaleDto findsalesById(Long id) throws EntityNotFoundException;
    public SaleDto persistsales(SaleDto saleDto) throws EntityNotFoundException, IOException;
    public SaleDto updatesales(Long id, SaleDto saleDto) throws EntityNotFoundException  ;
    public ResponseDto deletesalesById(Long id) throws EntityNotFoundException ;

    SaleDto modifySaleDtoStatusToAccepted(Long saleId);

    SaleDto modifySaleDtoStatusToReported(Long saleId);

    SaleDto modifySaleDtoStatusToCancelled(Long saleId);
}
