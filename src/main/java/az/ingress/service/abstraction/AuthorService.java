package az.ingress.service.abstraction;

import az.ingress.dao.entity.AuthorEntity;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.request.RegistrationRequest;

public interface AuthorService {

    void createAuthor(CreateUserRequest userRequest);

    void subscribe(Long id, Long studentId);

    AuthorEntity findAuthorByIdIfExists(Long id);
}
