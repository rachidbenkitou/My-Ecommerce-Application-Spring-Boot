package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.services.inter.ClientOrderDetailsService;
import com.benkitoucoders.ecommerce.dtos.ClientOrderDetailsDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/clientOrderDetails")
@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:58213", allowCredentials = "true")
public class ClientOrderDetailsController {
    private ClientOrderDetailsService clientOrderDetailsService;

    @GetMapping
    public ResponseEntity<List<ClientOrderDetailsDto>> getClientOrderDetailsByQuery(@RequestParam(name = "orderId", required = false) Long orderId) {
        return ResponseEntity.ok().body(clientOrderDetailsService.getClientOrderDetailsByQuery(orderId));
    }

    @PostMapping
    public ResponseEntity<ClientOrderDetailsDto> addClientOrderDetails(
            @RequestBody ClientOrderDetailsDto clientOrderDetailsDto) throws IOException {
        return ResponseEntity.ok().body(clientOrderDetailsService.addClientOrderDetails(clientOrderDetailsDto));
    }

    @DeleteMapping("/{clientOrderDetailsId}")
    public ResponseEntity<?> deleteClientOrderDetailsById(@PathVariable Long clientOrderDetailsId) {
        return ResponseEntity.ok().body(clientOrderDetailsService.deleteClientOrderDetailsById(clientOrderDetailsId));
    }

}

