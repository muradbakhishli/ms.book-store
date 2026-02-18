package az.ingress.service.abstraction;

import az.ingress.model.request.RegistrationRequest;
import az.ingress.model.response.UserResponse;

public interface UserService {

    void registrationUser(RegistrationRequest registrationRequest);

    UserResponse getUserByEmail(String email);
}
