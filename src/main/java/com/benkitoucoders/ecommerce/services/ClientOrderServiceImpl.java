package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.dao.ClientDao;
import com.benkitoucoders.ecommerce.dao.ClientOrderDao;
import com.benkitoucoders.ecommerce.dtos.ClientOrderDetailsDto;
import com.benkitoucoders.ecommerce.dtos.ClientOrderDto;
import com.benkitoucoders.ecommerce.dtos.ProductDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.entities.Client;
import com.benkitoucoders.ecommerce.entities.ClientOrder;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.exceptions.NoStockExistException;
import com.benkitoucoders.ecommerce.mappers.ClientOrderMapper;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderDetailsService;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderService;
import com.benkitoucoders.ecommerce.services.inter.ProductService;
import com.benkitoucoders.ecommerce.services.pdfs.DeliveredOrderStatement;
import com.benkitoucoders.ecommerce.utils.OrderStatusIds;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ClientOrderServiceImpl implements ClientOrderService {
    private final ClientOrderDao clientOrderDao;
    private final ClientDao clientDao;
    private final ClientOrderMapper clientOrderMapper;
    private final DeliveredOrderStatement deliveredOrderStatement;
    private final ProductService productService;
    private final ClientOrderDetailsService clientOrderDetailsService;

    public ClientOrderServiceImpl(ClientOrderDao clientOrderDao,
                                  ClientDao clientDao, ClientOrderMapper clientOrderMapper,
                                  DeliveredOrderStatement deliveredOrderStatement,
                                  ProductService productService,
                                  @Qualifier("clientOrderDetailsServiceImpl") ClientOrderDetailsService clientOrderDetailsService) {
        this.clientOrderDao = clientOrderDao;
        this.clientDao = clientDao;
        this.clientOrderMapper = clientOrderMapper;
        this.deliveredOrderStatement = deliveredOrderStatement;
        this.productService = productService;
        this.clientOrderDetailsService = clientOrderDetailsService;
    }

    @Override
    public List<ClientOrderDto> getClientOrdersByQuery(Long orderId, Long clientId, Long orderStatusId, LocalDateTime dateCreation, LocalDateTime dateUpdate) {
        return clientOrderDao.findAllClientOrders(orderId, clientId, dateCreation, dateUpdate, orderStatusId);
    }

    @Override
    public List<ClientOrderDto> getClientOrdersByClientIdLong(String username) {
        Client client=clientDao.findClientByEmail(username);
        if (client==null) throw new EntityNotFoundException("The client with username: "+username+" is not found");
        Long clientId= client.getId();
        return clientOrderDao.findAllClientOrders(null, clientId, null, null, null);
    }
    @Override
    public ClientOrderDto getClientOrderById(Long id) {
        List<ClientOrderDto> clientOrderDtoList = clientOrderDao.findAllClientOrders(id, null, null, null, null);
        return clientOrderDtoList.stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("The clientOrder with the id %d is not found.", id)));
    }
    @Override
    public ClientOrderDto getClientOrderByClientId(Long clientId) {
        List<ClientOrderDto> clientOrderDtoList = clientOrderDao.findAllClientOrders(null, clientId, null, null, null);
        return clientOrderDtoList.stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("The clientOrder with the clientId %d is not found.", clientId)));
    }
    @Override
    public ClientOrderDto addClientOrder(ClientOrderDto clientOrderDto) throws IOException {
        double orderTotalPrice = 0;
        clientOrderDto.setId(null);
        clientOrderDto.setClientOrderStatusId(OrderStatusIds.IN_PROGRESS);

        // Map to store product IDs and their corresponding quantities
        Map<Long, Integer> productQuantities = new HashMap<>();

        ClientOrderDto savedClientOrderDto = clientOrderMapper.modelToDto(clientOrderDao.save(clientOrderMapper.dtoToModel(clientOrderDto)));
        // Make sure if the product is out of stock
        for (ClientOrderDetailsDto clientOrderDetailsDto : clientOrderDto.getClientOrderDetailsDtos()) {


            clientOrderDetailsDto.setClientOrderId(savedClientOrderDto.getId());
            clientOrderDetailsDto.setPrice(clientOrderDetailsDto.getPrice() * clientOrderDetailsDto.getQuantity());
            ClientOrderDetailsDto clientOrderDetailsDto1= clientOrderDetailsService.addClientOrderDetails(clientOrderDetailsDto);

            orderTotalPrice += (clientOrderDetailsDto1.getPrice());


            long productId = clientOrderDetailsDto.getProductId();
            int orderQuantity = clientOrderDetailsDto.getQuantity();

            // Fetch product and its quantity
            ProductDto productDto = productService.getProductById(productId);
            int availableQuantity = productDto.getQuantity();

            // Check if there's enough stock for the product
            if (availableQuantity < orderQuantity) {
                throw new NoStockExistException("Sorry, there is no stock for the product: " + productDto.getName());
            }

            // Update product quantity map for later use
            productQuantities.put(productId, availableQuantity - orderQuantity);

            // No need to update the product in the database yet, we'll do it after validation
        }

        // Validate if the ClientOrder price is equal to clientOrderDetails total ordered products price (like this we will sure that the order pricing is logic)
        if (orderTotalPrice != savedClientOrderDto.getTotalPrice()) {
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
        ClientOrderDto oldClientOrderDto = getClientOrderById(id);
        clientOrderDto.setId(oldClientOrderDto.getId());
        ClientOrder updatedClientOrder = clientOrderDao.save(clientOrderMapper.dtoToModel(clientOrderDto));
        return clientOrderMapper.modelToDto(updatedClientOrder);
    }

    @Override
    public ResponseDto deleteClientOrderById(Long id) {
        getClientOrderById(id);
        clientOrderDao.deleteById(id);
        return ResponseDto.builder()
                .message("ClientOrder successfully deleted.")
                .build();
    }

    private ClientOrder retrieveClientOrderById(Long clientOrderId) {
        return clientOrderDao.findById(clientOrderId)
                .orElseThrow(() -> new RuntimeException(String.format("The clientOrder with id %d not found.", clientOrderId)));
    }

    @Override
    public ClientOrderDto modifyClientOrderDtoStatusToAccepted(Long clientOrderId) throws DocumentException, FileNotFoundException {
            ClientOrder clientOrder = retrieveClientOrderById(clientOrderId);
            clientOrder.setClientOrderStatusId(OrderStatusIds.ACCEPTED);
            ClientOrderDto clientOrderDto = clientOrderMapper.modelToDto(clientOrderDao.save(clientOrder));
            deliveredOrderStatement.generateDeliveredOrderStatement(clientOrderId, clientOrderDto, null, true);
            return clientOrderDto;

    }

    @Override
    public ClientOrderDto modifyClientOrderDtoStatusToReported(Long clientOrderId) {
        try {
            ClientOrder clientOrder = retrieveClientOrderById(clientOrderId);
            clientOrder.setClientOrderStatusId(OrderStatusIds.REPORTED);
            return clientOrderMapper.modelToDto(clientOrderDao.save(clientOrder));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while modifying clientOrder status to reported.", e);
        }
    }

    @Override
    public ClientOrderDto modifyClientOrderDtoStatusToCancelled(Long clientOrderId) {
        try {
            ClientOrder clientOrder = retrieveClientOrderById(clientOrderId);
            clientOrder.setClientOrderStatusId(OrderStatusIds.CANELLED);
            return clientOrderMapper.modelToDto(clientOrderDao.save(clientOrder));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while modifying clientOrder status to cancelled.", e);
        }
    }
}