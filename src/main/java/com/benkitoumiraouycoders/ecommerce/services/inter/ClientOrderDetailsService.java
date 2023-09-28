package com.benkitoumiraouycoders.ecommerce.services.inter;

import com.benkitoumiraouycoders.ecommerce.dtos.ClientOrderDetailsDto;
import com.benkitoumiraouycoders.ecommerce.handlers.ResponseDto;

import java.io.IOException;
import java.util.List;

public interface ClientOrderDetailsService {
    List<ClientOrderDetailsDto> getClientOrderDetailsByQuery();

    ClientOrderDetailsDto addClientOrderDetails(ClientOrderDetailsDto clientOrderDetailsDto) throws IOException;

    ResponseDto deleteClientOrderDetailsById(Long id);
}
