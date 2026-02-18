package az.ingress.mapper;

import az.ingress.dao.entity.RoleEntity;
import az.ingress.dao.entity.UserEntity;
import az.ingress.model.request.RegistrationRequest;
import az.ingress.model.response.RoleResponse;
import az.ingress.model.response.UserResponse;

import java.util.Set;
import java.util.stream.Collectors;

import static az.ingress.mapper.RoleMapper.ROLE_MAPPER;

public enum UserMapper {

    USER_MAPPER;

    public UserEntity toUserEntity(RegistrationRequest registrationRequest, RoleEntity roleEntity) {
        return UserEntity.builder()
                .username(registrationRequest.getEmail())
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .roles(Set.of(roleEntity))
                .build();
    }

    public UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .userId(userEntity.getId())
                .email(userEntity.getUsername())
                .roles(userEntity.getRoles().stream().map(ROLE_MAPPER::toRoleResponse).collect(Collectors.toSet()))
                .build();
    }
}
