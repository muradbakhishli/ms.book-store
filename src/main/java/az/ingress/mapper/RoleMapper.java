package az.ingress.mapper;

import az.ingress.dao.entity.RoleEntity;
import az.ingress.model.enums.UserRole;
import az.ingress.model.response.RoleResponse;

public enum RoleMapper {

    ROLE_MAPPER;

    public RoleEntity toRoleEntity(UserRole role) {
        return RoleEntity.builder()
                .authority(role.name())
                .build();
    }

    public RoleResponse toRoleResponse(RoleEntity role) {
        return RoleResponse.builder()
                .authority(role.getAuthority())
                .build();
    }
}
