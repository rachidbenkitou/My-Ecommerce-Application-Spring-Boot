package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.services.inter.ClientOrderService;
import com.benkitoucoders.ecommerce.dtos.ClientOrderDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/clientOrders")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:58213", allowCredentials = "true")
public class ClientOrderController {
    private ClientOrderService clientOrderService;

    @GetMapping
    public ResponseEntity<List<ClientOrderDto>> getClientOrdersByQuery(@RequestParam(name = "orderId", required = false) Long orderId,
                                                                       @RequestParam(name = "clientId", required = false) Long clientId,
                                                                       @RequestParam(name = "dateCreation", required = false) LocalDateTime dateCreation,
                                                                       @RequestParam(name = "dateUpdate", required = false) LocalDateTime dateUpdate,
                                                                       @RequestParam(name = "orderStatusId", required = false) Long orderStatusId
    ) throws DocumentException, FileNotFoundException {
        return ResponseEntity.ok().body(clientOrderService.getClientOrdersByQuery(orderId, clientId, orderStatusId, dateCreation,
                dateUpdate));
    }

    @GetMapping("/{clientOrderId}")
    public ResponseEntity<ClientOrderDto> getClientOrderById(@PathVariable Long clientOrderId) {
        return ResponseEntity.ok().body(clientOrderService.getClientOrderById(clientOrderId));
    }

    @PostMapping
    public ResponseEntity<ClientOrderDto> addClientOrder(
            @RequestBody ClientOrderDto clientOrderDto) throws IOException {
        return ResponseEntity.ok().body(clientOrderService.addClientOrder(clientOrderDto));
    }


    @PutMapping("/{clientOrderId}")
    public ResponseEntity<ClientOrderDto> updateClientOrder(@PathVariable Long clientOrderId, @RequestBody ClientOrderDto clientOrderDto) {
        return ResponseEntity.ok().body(clientOrderService.updateClientOrder(clientOrderId, clientOrderDto));
    }

    @DeleteMapping("/{clientOrderId}")
    public ResponseEntity<?> deleteClientOrderById(@PathVariable Long clientOrderId) {
        return ResponseEntity.ok().body(clientOrderService.deleteClientOrderById(clientOrderId));
    }

    @PatchMapping("/{clientOrderId}/accepted")
    public ResponseEntity<ClientOrderDto> modifyClientOrderStatusToAccepted(@PathVariable Long clientOrderId) throws EntityNotFoundException {
        ClientOrderDto updatedClientOrder = clientOrderService.modifyClientOrderDtoStatusToAccepted(clientOrderId);
        return ResponseEntity.ok(updatedClientOrder);
    }

    @PatchMapping("/{clientOrderId}/reported")
    public ResponseEntity<ClientOrderDto> modifyClientOrderStatusToReported(@PathVariable Long clientOrderId) throws EntityNotFoundException {
        ClientOrderDto updatedClientOrder = clientOrderService.modifyClientOrderDtoStatusToReported(clientOrderId);
        return ResponseEntity.ok(updatedClientOrder);
    }

    @PatchMapping("/{clientOrderId}/cancelled")
    public ResponseEntity<ClientOrderDto> modifyClientOrderStatusToCancelled(@PathVariable Long clientOrderId) throws EntityNotFoundException {
        ClientOrderDto updatedClientOrder = clientOrderService.modifyClientOrderDtoStatusToCancelled(clientOrderId);
        return ResponseEntity.ok(updatedClientOrder);
    }
}
