package com.benkitoucoders.myecommerce.dtos.address;

import lombok.*;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class AddressRequestDto implements Serializable {

    private Long id;
    private String city;
    private String street;
}
