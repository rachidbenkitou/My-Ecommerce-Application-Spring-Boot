package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.criteria.SaleDetailsCriteria;
import com.benkitoucoders.ecommerce.dtos.SaleDetailsDto;
import com.benkitoucoders.ecommerce.services.inter.SaleDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/saleDetails")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:58213", allowCredentials = "true")
public class SaleDetailsController {

    private SaleDetailsService saleDetailsService;

    @GetMapping
    public ResponseEntity<List<SaleDetailsDto>> getSaleDetailsByQuery(
            @RequestParam(name = "saleId", required = false) Long saleId,
            @RequestParam(name = "id", required = false) Long id
    ) {
        SaleDetailsCriteria saleDetailsCriteria = SaleDetailsCriteria.builder()
                .id(id)
                .saleId(saleId)
                .build();
        return ResponseEntity.ok().body(saleDetailsService.findSaleDetailsByCriteria(saleDetailsCriteria));
    }

    @GetMapping("/findBySaleId/{id}")
    public ResponseEntity<List<SaleDetailsDto>> getSaleDetailsByQueryBySaleId(
            @PathVariable Long id
    ) {
        SaleDetailsCriteria saleDetailsCriteria = SaleDetailsCriteria.builder()
                .saleId(id)
                .build();
        return ResponseEntity.ok().body(saleDetailsService.findSaleDetailsByCriteria(saleDetailsCriteria));
    }

    @PostMapping
    public ResponseEntity<SaleDetailsDto> addSaleDetails(
            @RequestBody SaleDetailsDto saleDetailsDto) throws IOException {
        return ResponseEntity.ok().body(saleDetailsService.persistSaleDetails(saleDetailsDto));
    }

    @DeleteMapping("/{saleDetailsId}")
    public ResponseEntity<?> deleteSaleDetailsById(@PathVariable Long saleDetailsId) {
        return ResponseEntity.ok().body(saleDetailsService.deleteSaleDetailsById(saleDetailsId));
    }

}
