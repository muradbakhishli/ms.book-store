package az.ingress.service.strategy;

import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.request.RegistrationRequest;
import az.ingress.service.abstraction.AuthorService;
import az.ingress.service.abstraction.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationStrategy {

    private final AuthorService authorService;
    private final StudentService studentService;

    @Async
    public void registrationStrategy(RegistrationRequest registrationRequest) {
        switch (registrationRequest.getRole()) {
            case AUTHOR -> authorService.createAuthor(CreateUserRequest.of(registrationRequest));
            case STUDENT -> studentService.createStudent(CreateUserRequest.of(registrationRequest));
        }
    }
}
