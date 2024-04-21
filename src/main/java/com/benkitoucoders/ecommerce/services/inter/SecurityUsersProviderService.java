package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.dtos.LoginResponseDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.dtos.SecurityUserDto;
import com.benkitoucoders.ecommerce.dtos.UserPasswordDto;
import com.benkitoucoders.ecommerce.entities.Role;
import org.keycloak.admin.client.resource.UsersResource;

import java.util.List;

public interface SecurityUsersProviderService {
    List<SecurityUserDto> getAllUsers(String accessToken);

    SecurityUserDto getUserByUsername(String username, String token);

    SecurityUserDto getUserById(String id, String token);

    void setUpPasswordToUser(UserPasswordDto userPasswordDto, String userId, String token);

    SecurityUserDto addUser(SecurityUserDto user, String token);

    ResponseDto updateUser(SecurityUserDto user, String id, String token);

    ResponseDto deleteUserById(String id, String token);

    LoginResponseDto login(String grantType, String clientId,String clientSecret, String username, String password);

    ResponseDto logout(String token, String clientId, String refreshToken);

    ResponseDto assignRoleToUser(String userId, List<Role> roles, String token);

    UsersResource getUsersResource();

    void forgotPassword(String username);
}
