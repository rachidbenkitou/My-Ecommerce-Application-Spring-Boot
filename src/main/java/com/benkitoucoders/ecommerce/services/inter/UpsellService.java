package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.dtos.UpsellDto;
import com.benkitoucoders.ecommerce.entities.Upsell;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;


public interface UpsellService {
    Page<Upsell> getUpsells(Pageable pageable);
    // Method to search Upsells by productId
    Page<UpsellDto> searchUpsellsByProductId(Long productId, Pageable pageable);

    // Method to search Upsells by packageId
    Page<UpsellDto> searchUpsellsByPackageId(Long packageId, Pageable pageable);

    UpsellDto getUpsellById(Long id);

    UpsellDto addUpsell(UpsellDto upsellDto) throws IOException;

    UpsellDto updateUpsell(Long id, UpsellDto upsellDto);

    ResponseDto deleteUpsellById(Long id);
}
