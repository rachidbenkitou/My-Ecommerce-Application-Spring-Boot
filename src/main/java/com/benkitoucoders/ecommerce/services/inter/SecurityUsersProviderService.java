package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.dtos.SecurityUserDto;

import java.util.List;

public interface SecurityUsersProviderService {
    List<SecurityUserDto> getAllUsers(String accessToken);

    SecurityUserDto getUserByUsername(String username, String token);

    SecurityUserDto addUser(SecurityUserDto user, String token);

    ResponseDto updateUser(SecurityUserDto user, String id, String token);

    ResponseDto deleteUserById(String id, String token);

    ResponseDto login(String grantType, String clientId, String username, String password);

    ResponseDto logout(String token);
}
