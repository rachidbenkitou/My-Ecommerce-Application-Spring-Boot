package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.entities.Role;

import java.util.List;

public interface SecurityRolesProviderService {
    List<Role> getAllRoles(String accessToken);

    Role getRoleByName(String name, String token);

    Role addRole(Role role, String token);

    ResponseDto deleteRoleByName(String name, String token);

}
