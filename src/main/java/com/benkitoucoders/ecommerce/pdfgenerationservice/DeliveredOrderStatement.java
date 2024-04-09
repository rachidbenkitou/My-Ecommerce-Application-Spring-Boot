package com.benkitoucoders.ecommerce.pdfgenerationservice;

import com.benkitoucoders.ecommerce.dtos.ClientOrderDto;
import com.benkitoucoders.ecommerce.dtos.SaleDto;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderService;
import com.benkitoucoders.ecommerce.services.inter.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveredOrderStatement {
    private final ClientOrderService clientOrderService;
    private final SaleService saleService;

    private static final String FILE = "/pdfs/deliveredOrderStatement.pdf";

    // In our app we handle two types of order, when the client is registered in our app, or it is just a sale the customer is not registered
    // So if isClientOrder we will make search in ClientOrderService else SaleService
    public void generateDeliveredOrderStatement(Long orderId, boolean isClientOrder) {
        if (isClientOrder) {
            ClientOrderDto clientOrderDto = clientOrderService.getClientOrderById(orderId);
        } else {
            SaleDto saleDto = saleService.findsalesById(orderId);
        }


    }

}
