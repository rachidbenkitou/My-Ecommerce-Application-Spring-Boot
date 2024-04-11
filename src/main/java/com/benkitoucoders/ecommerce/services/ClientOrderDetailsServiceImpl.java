package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.dao.ClientOrderDetailsDao;
import com.benkitoucoders.ecommerce.dtos.ClientOrderDetailsDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.entities.ClientOrderDetails;
import com.benkitoucoders.ecommerce.mappers.ClientOrderDetailsMapper;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientOrderDetailsServiceImpl implements ClientOrderDetailsService {
    private final ClientOrderDetailsDao clientOrderDetailsDao;
    private final ClientOrderDetailsMapper clientOrderDetailsMapper;

    @Override
    public List<ClientOrderDetailsDto> getClientOrderDetailsByQuery(Long orderId) {
        return clientOrderDetailsDao.findAllClientOrderDetails(orderId);
    }

    @Override
    public ClientOrderDetailsDto addClientOrderDetails(ClientOrderDetailsDto clientOrderDetailsDto) {
        clientOrderDetailsDto.setId(null);

        ClientOrderDetails clientOrderDetails = clientOrderDetailsMapper.dtoToModel(clientOrderDetailsDto);
        return clientOrderDetailsMapper.modelToDto(clientOrderDetailsDao.save(clientOrderDetails));
    }

    @Override
    public ResponseDto deleteClientOrderDetailsById(Long id) {
        clientOrderDetailsDao.deleteById(id);
        return ResponseDto.builder()
                .message("ClientOrderDetailsController successfully deleted.")
                .build();
    }
}
