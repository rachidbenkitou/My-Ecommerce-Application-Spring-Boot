package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.services.inter.SaleDetailsService;
import com.benkitoucoders.ecommerce.criteria.SaleDetailsCriteria;
import com.benkitoucoders.ecommerce.dao.SaleDetailsDao;
import com.benkitoucoders.ecommerce.dtos.SaleDetailsDto;
import com.benkitoucoders.ecommerce.mappers.SaleDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleDetailServiceImpl implements SaleDetailsService {

    @Autowired
    private SaleDetailsDao saleDetailsDao;

    @Autowired
    private SaleDetailsMapper saleDetailsMapper;
    @Autowired
    private ProductServiceImpl productService;

    @Override
    public List<SaleDetailsDto> findSaleDetailsByCriteria(SaleDetailsCriteria saleDetailsCriteria) throws EntityNotFoundException {
        return saleDetailsDao.getSaleDetailsByQuery(saleDetailsCriteria.getId(), saleDetailsCriteria.getSaleId());
    }

    @Override
    public SaleDetailsDto findSaleDetailsById(Long id) throws EntityNotFoundException {
        SaleDetailsCriteria saleDetailsCriteria = new SaleDetailsCriteria();
        saleDetailsCriteria.setId(id);
        List<SaleDetailsDto> saleDetailsDtoList = findSaleDetailsByCriteria(saleDetailsCriteria);
        if (saleDetailsDtoList != null && !saleDetailsDtoList.isEmpty()) {
            return saleDetailsDtoList.get(0);
        } else {
            throw new EntityNotFoundException("The details of sale with the id " + id + "  is not found.");
        }
    }

    @Override
    public List<SaleDetailsDto> findAllSaleDetailsById(Long id) throws EntityNotFoundException {
        SaleDetailsCriteria saleDetailsCriteria = new SaleDetailsCriteria();
        saleDetailsCriteria.setId(id);
        List<SaleDetailsDto> saleDetailsDtoList = findSaleDetailsByCriteria(saleDetailsCriteria);
        if (saleDetailsDtoList != null && !saleDetailsDtoList.isEmpty()) {
            return saleDetailsDtoList;
        } else {
            throw new EntityNotFoundException("The details of sale with the id " + id + "  is not found.");
        }
    }
    @Override
    public SaleDetailsDto persistSaleDetails(SaleDetailsDto saleDetailsDto) throws EntityNotFoundException {
        return saleDetailsMapper.modelToDto(saleDetailsDao.save(saleDetailsMapper.dtoToModel(saleDetailsDto)));
    }

    @Override
    public SaleDetailsDto updatesaleDetails(Long id, SaleDetailsDto saleDetailsDto) throws EntityNotFoundException {
        SaleDetailsDto saleDetailsDto1 = findSaleDetailsById(id);
        saleDetailsDto1.setId(id);
        saleDetailsDto1.setDateUpdate(LocalDateTime.now());
        return saleDetailsMapper.modelToDto(saleDetailsDao.save(saleDetailsMapper.dtoToModel(saleDetailsDto1)));
    }

    @Override
    public ResponseDto deleteSaleDetailsById(Long id) throws EntityNotFoundException {
        ResponseDto responseDto = new ResponseDto();
        SaleDetailsDto saleDetailsDto = findSaleDetailsById(id);
        // dans la supprission de vente si la vente est payer c'est bon si la vente est annuler il faut augmenter la quantite de produit 
        //mais je prefere de metre des api pour l'annulation des produit 
        saleDetailsDao.deleteById(id);
        responseDto.setMessage("SaleDetails is deleted succesfully!");
        return responseDto;
    }

}
