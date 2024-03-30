package com.benkitoucoders.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    @JsonProperty("username")
    private String email;
    private String phoneNumber;
    private Long statusId;
    private String statusName;
    private String statusColor;
    private String password;

    ClientDto(Long id, String firstName, String lastName,
              String address, Long statusId, String statusName, String email, String phoneNumber, String statusColor) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.statusId = statusId;
        this.statusName = statusName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.statusColor = statusColor;
    }
}
