package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.dao.ClientOrderDao;
import com.benkitoucoders.ecommerce.dtos.*;
import com.benkitoucoders.ecommerce.exceptions.NoStockExistException;
import com.benkitoucoders.ecommerce.mappers.ClientOrderMapper;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderDetailsService;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderService;
import com.benkitoucoders.ecommerce.services.inter.PackageService;
import com.benkitoucoders.ecommerce.services.inter.ProductService;
import com.benkitoucoders.ecommerce.services.pdfs.DeliveredOrderStatement;
import com.benkitoucoders.ecommerce.utils.OrderStatusIds;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PackageClientOrderServiceImpl implements ClientOrderService {
    // TODO I KNOW THAT I DID NOT RESPECT STRATEGY PATTERN, BUT I WILL BE IMPLEMENT IT LATER
    private final ClientOrderDao clientOrderDao;
    private final ClientOrderMapper clientOrderMapper;
    private final DeliveredOrderStatement deliveredOrderStatement;
    private final ProductService productService;
    private final ClientOrderDetailsService clientOrderDetailsService;
    private final PackageService packageService;

    @Override
    public List<ClientOrderDto> getClientOrdersByQuery(Long orderId, Long clientId, Long orderStatusId, LocalDateTime dateCreation, LocalDateTime dateUpdate) throws DocumentException, FileNotFoundException {
        return null;
    }

    @Override
    public List<ClientOrderDto> getClientOrdersByClientIdLong(String  username) {
        return null;
    }

    @Override
    public ClientOrderDto getClientOrderById(Long id) {
        return null;
    }

    @Override
    public ClientOrderDto getClientOrderByClientId(Long clientId) {
        return null;
    }

    public ClientOrderDto addClientOrder(ClientOrderDto clientOrderDto) throws IOException {
        clientOrderDto.setId(null);
        clientOrderDto.setClientOrderStatusId(OrderStatusIds.IN_PROGRESS);

        double totalPrice = 0.0;
        // Map to store product IDs and their corresponding quantities
        Map<Long, Integer> productQuantities = new HashMap<>();

        ClientOrderDto savedClientOrderDto = clientOrderMapper.modelToDto(clientOrderDao.save(clientOrderMapper.dtoToModel(clientOrderDto)));
        // Make sure if the product is out of stock
        for (ClientOrderDetailsDto clientOrderDetailsDto : clientOrderDto.getClientOrderDetailsDtos()) {


            clientOrderDetailsDto.setClientOrderId(savedClientOrderDto.getId());
            clientOrderDetailsDto.setPrice(clientOrderDetailsDto.getPrice());
            ClientOrderDetailsDto clientOrderDetailsDto1 = clientOrderDetailsService.addClientOrderDetails(clientOrderDetailsDto);

            totalPrice += clientOrderDetailsDto1.getPrice();


            long packageId = clientOrderDetailsDto.getPackageId();

            int orderQuantity = clientOrderDetailsDto.getQuantity();

            PackageDto packageDto = packageService.findPackagesById(packageId);


            for (ProductDto productDto : packageDto.getProductDtos()) {
                int availableQuantity = productDto.getQuantity();

                // Check if there's enough stock for the product
                if (availableQuantity < orderQuantity) {
                    throw new NoStockExistException("Sorry, there is no stock for the product: " + productDto.getName());
                }
                // Update product quantity map for later use
                productQuantities.put(productDto.getId(), availableQuantity - orderQuantity);
            }


            // No need to update the product in the database yet, we'll do it after validation
        }

        if (totalPrice != savedClientOrderDto.getTotalPrice()) {
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

        return savedClientOrderDto;
    }


    @Override
    public ClientOrderDto updateClientOrder(Long id, ClientOrderDto clientOrderDto) {
        return null;
    }

    @Override
    public ResponseDto deleteClientOrderById(Long id) {
        return null;
    }

    @Override
    public ClientOrderDto modifyClientOrderDtoStatusToAccepted(Long clientOrderId) {
        return null;
    }

    @Override
    public ClientOrderDto modifyClientOrderDtoStatusToReported(Long clientOrderId) {
        return null;
    }

    @Override
    public ClientOrderDto modifyClientOrderDtoStatusToCancelled(Long clientOrderId) {
        return null;
    }
}
