package az.ingress.service.concrete;

import az.ingress.dao.entity.AuthorEntity;
import az.ingress.dao.repository.AuthorRepository;
import az.ingress.exception.ErrorMessage;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.request.RegistrationRequest;
import az.ingress.service.abstraction.AuthorService;
import az.ingress.service.abstraction.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static az.ingress.exception.ErrorMessage.AUTHOR_NOT_FOUND;
import static az.ingress.mapper.AuthorMapper.AUTHOR_MAPPER;

@Service
@RequiredArgsConstructor
public class AuthorServiceHandler implements AuthorService {

    private final AuthorRepository authorRepository;
    private final StudentService studentService;

    @Override
    public void createAuthor(CreateUserRequest registrationRequest) {
        var author = AUTHOR_MAPPER.toAuthorEntity(registrationRequest);
        authorRepository.save(author);
    }

    @Override
    @Transactional
    public void subscribe(Long id, Long studentId) {
        var author = findAuthorByIdIfExists(id);
        var student = studentService.findStudentByIdIfExists(studentId);
        author.setStudents(Set.of(student));
        student.setAuthors(Set.of(author));
    }

    @Override
    public AuthorEntity findAuthorByIdIfExists(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AUTHOR_NOT_FOUND.getMessage()));
    }
}
