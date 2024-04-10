package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.dao.ClientOrderDao;
import com.benkitoucoders.ecommerce.dtos.ClientOrderDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.entities.ClientOrder;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.mappers.ClientOrderMapper;
import com.benkitoucoders.ecommerce.pdfgenerationservice.DeliveredOrderStatement;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderService;
import com.benkitoucoders.ecommerce.utils.OrderStatusIds;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {
    private final ClientOrderDao clientOrderDao;
    private final ClientOrderMapper clientOrderMapper;
    private final DeliveredOrderStatement deliveredOrderStatement;

    @Override
    public List<ClientOrderDto> getClientOrdersByQuery(Long orderId, Long clientId, Long orderStatusId, LocalDateTime dateCreation, LocalDateTime dateUpdate) {
        return clientOrderDao.findAllClientOrders(orderId, clientId, dateCreation, dateUpdate, orderStatusId);
    }

    @Override
    public ClientOrderDto getClientOrderById(Long id) {
        List<ClientOrderDto> clientOrderDtoList = clientOrderDao.findAllClientOrders(id, null, null, null, null);
        return clientOrderDtoList.stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("The clientOrder with the id %d is not found.", id)));
    }

    @Override
    public ClientOrderDto addClientOrder(ClientOrderDto clientOrderDto) throws IOException {
        clientOrderDto.setId(null);
        clientOrderDto.setClientOrderStatusId(OrderStatusIds.IN_PROGRESS);
        return clientOrderMapper.modelToDto(clientOrderDao.save(clientOrderMapper.dtoToModel(clientOrderDto)));
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
    public ClientOrderDto modifyClientOrderDtoStatusToAccepted(Long clientOrderId) {
        try {
            ClientOrder clientOrder = retrieveClientOrderById(clientOrderId);
            clientOrder.setClientOrderStatusId(OrderStatusIds.ACCEPTED);
            ClientOrderDto clientOrderDto = clientOrderMapper.modelToDto(clientOrderDao.save(clientOrder));
            deliveredOrderStatement.generateDeliveredOrderStatement(clientOrderId, clientOrderDto,true);
            return clientOrderDto;

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while modifying clientOrder status to accepted.", e);

        }
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
