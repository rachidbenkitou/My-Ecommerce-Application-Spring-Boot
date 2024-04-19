package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.criteria.SaleCriteria;
import com.benkitoucoders.ecommerce.dao.SaleDao;
import com.benkitoucoders.ecommerce.dtos.ProductDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.dtos.SaleDetailsDto;
import com.benkitoucoders.ecommerce.dtos.SaleDto;
import com.benkitoucoders.ecommerce.entities.Sale;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.exceptions.NoStockExistException;
import com.benkitoucoders.ecommerce.mappers.SaleMapper;
import com.benkitoucoders.ecommerce.services.inter.SaleDetailsService;
import com.benkitoucoders.ecommerce.services.inter.SaleService;
import com.benkitoucoders.ecommerce.services.pdfs.DeliveredOrderStatement;
import com.benkitoucoders.ecommerce.utils.OrderStatusIds;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleDao saleDao;

    private final SaleMapper saleMapper;
    private final ProductServiceImpl productService;
    private final DeliveredOrderStatement deliveredOrderStatement;
    private final SaleDetailsService saleDetailsService;

    public List<SaleDto> findsalesByCriteria(SaleCriteria saleCriteria) throws EntityNotFoundException {
        return saleDao.getSalesByQuery(saleCriteria.getId(), saleCriteria.getSaleStatusId());
    }

    @Override
    public SaleDto findsalesById(Long id) throws EntityNotFoundException {
        SaleCriteria saleCriteria = new SaleCriteria();
        saleCriteria.setId(id);
        List<SaleDto> saleDtoList = findsalesByCriteria(saleCriteria);
        if (saleDtoList != null && !saleDtoList.isEmpty()) {
            return saleDtoList.get(0);
        } else {
            throw new EntityNotFoundException("The sale with the id " + id + "  is not found.");
        }
    }

    @Override
    public SaleDto persistsales(SaleDto saleDto) throws IOException, IOException {
        double orderTotalPrice = 0;
        saleDto.setId(null);
        saleDto.setSaleStatusId(OrderStatusIds.IN_PROGRESS);
        // Map to store product IDs and their corresponding quantities
        Map<Long, Integer> productQuantities = new HashMap<>();

        // Save the sale
        Sale savedSale = saleDao.save(saleMapper.dtoToModel(saleDto));

        // Iterate over sale details
        for (SaleDetailsDto saleDetailsDto : saleDto.getSaleDetails()) {

            orderTotalPrice += (saleDetailsDto.getPrice() * saleDetailsDto.getQuantity());

            saleDetailsDto.setId(null); // Set ID to null for new entity
            saleDetailsDto.setSaleId(savedSale.getId()); // Set sale ID
            saleDetailsDto.setPrice(saleDetailsDto.getPrice() * saleDetailsDto.getQuantity());
            saleDetailsService.persistSaleDetails(saleDetailsDto); // Save SaleDetailsDto

            // Start Validation if the quantity is in stock or not
            long productId = saleDetailsDto.getProductId();
            int soldQuantity = saleDetailsDto.getQuantity();

            // Fetch product and its quantity
            ProductDto productDto = productService.getProductById(productId);
            int availableQuantity = productDto.getQuantity();

            // Check if there's enough stock for the product
            if (availableQuantity < soldQuantity) {
                throw new NoStockExistException("Sorry, there is no stock for the product: " + productDto.getName());
            }

            // Update product quantity map for later use
            productQuantities.put(productId, availableQuantity - soldQuantity);
        }

        // Validate if the Sale price is equal to saleOrderDetails total ordered products price (like this we will sure that the order pricing is logic)
        if (orderTotalPrice != savedSale.getTotalPrice()) {
            throw new NoStockExistException("Sorry, impossible to affect this order, order total price  and some of order details price are not the same");
        }
        // Update the stock (decrease the quantity of the product)
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            long productId = entry.getKey();
            int newQuantity = entry.getValue();

            ProductDto productDto = productService.getProductById(productId);
            productDto.setQuantity(newQuantity);
            productService.updateProduct(productId, productDto);
        }

        return saleMapper.modelToDto(savedSale);
    }


    @Override
    public SaleDto updatesales(Long id, SaleDto saleDto) throws EntityNotFoundException {
        SaleDto saleDto1 = findsalesById(id);
        saleDto1.setId(id);
        saleDto1.setDateUpdate(LocalDateTime.now());
        return saleMapper.modelToDto(saleDao.save(saleMapper.dtoToModel(saleDto1)));
    }

    @Override
    public ResponseDto deletesalesById(Long id) throws EntityNotFoundException {
        ResponseDto responseDto = new ResponseDto();
        findsalesById(id);
        // dans la supprission de vente si la vente est payer c'est bon si la vente est annuler il faut augmenter la quantite de produit
        //mais je prefere de metre des api pour l'annulation des produit
        saleDao.deleteById(id);
        responseDto.setMessage("Sale is successfully deleted!");
        return responseDto;
    }


    private Sale retrieveSaleById(Long saleId) {
        return saleDao.findById(saleId)
                .orElseThrow(() -> new RuntimeException(String.format("The sale with id %d not found.", saleId)));
    }

    @Override
    public SaleDto modifySaleDtoStatusToAccepted(Long saleId) {
        try {
            Sale sale = retrieveSaleById(saleId);
            sale.setSaleStatusId(OrderStatusIds.ACCEPTED);


            SaleDto saleDto = saleMapper.modelToDto(saleDao.save(sale));
            deliveredOrderStatement.generateDeliveredOrderStatement(saleId, null, saleDto, false);

            return saleDto;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while modifying sale status to accepted.", e);

        }
    }

    @Override
    public SaleDto modifySaleDtoStatusToReported(Long saleId) {
        try {
            Sale sale = retrieveSaleById(saleId);
            sale.setSaleStatusId(OrderStatusIds.REPORTED);
            return saleMapper.modelToDto(saleDao.save(sale));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while modifying sale status to reported.", e);
        }
    }

    @Override
    public SaleDto modifySaleDtoStatusToCancelled(Long saleId) {
        try {
            Sale sale = retrieveSaleById(saleId);
            sale.setSaleStatusId(OrderStatusIds.CANELLED);
            return saleMapper.modelToDto(saleDao.save(sale));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while modifying sale status to cancelled.", e);
        }
    }
}