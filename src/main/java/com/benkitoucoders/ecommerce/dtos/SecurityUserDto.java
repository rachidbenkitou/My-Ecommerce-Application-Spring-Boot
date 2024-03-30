package com.benkitoucoders.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class SecurityUserDto {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean emailVerified;
    private Long createdTimestamp;
    private Boolean enabled;
}
