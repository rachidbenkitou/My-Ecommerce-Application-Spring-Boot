package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.dtos.ClientDto;
import com.benkitoucoders.ecommerce.services.inter.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.benkitoucoders.ecommerce.utils.TokenManagement.extractToken;

@RestController
@RequestMapping("/clients")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:58213", allowCredentials = "true")
public class ClientController {
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDto>> getClientsByQuery(@RequestParam(name = "clientId", required = false) Long clientId,
                                                             @RequestParam(name = "firstName", required = false) String firstName,
                                                             @RequestParam(name = "lastName", required = false) String lastName,
                                                             @RequestParam(name = "email", required = false) String email,
                                                             @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
                                                             @RequestParam(name = "statusId", required = false) Long statusId
    ) {
        return ResponseEntity.ok().body(clientService.getClientsByQuery(clientId, firstName, lastName,
                email, phoneNumber, statusId));
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long clientId) {
        return ResponseEntity.ok().body(clientService.getClientById(clientId));
    }

    @PostMapping
    public ResponseEntity<ClientDto> addClient(
            HttpServletRequest request,
            @RequestBody ClientDto clientDto) throws IOException {
        String token = extractToken(request);
        return ResponseEntity.ok().body(clientService.addClient(clientDto, token));
    }


    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDto> updateClient
            (@PathVariable Long clientId, @RequestBody ClientDto clientDto) {
        return ResponseEntity.ok().body(clientService.updateClient(clientId, clientDto));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClientById(@PathVariable Long clientId, HttpServletRequest request
    ) {
        String token = extractToken(request);
        return ResponseEntity.ok().body(clientService.deleteClientById(clientId, token));
    }
}
