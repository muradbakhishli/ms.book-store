package az.ingress.mapper;

import az.ingress.dao.entity.AuthorEntity;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.request.RegistrationRequest;

public enum AuthorMapper {

    AUTHOR_MAPPER;

    public AuthorEntity toAuthorEntity(CreateUserRequest userRequest) {
        return AuthorEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .age(userRequest.getAge())
                .build();
    }
}
