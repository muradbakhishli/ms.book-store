package az.ingress.service.concrete;

import az.ingress.dao.entity.StudentEntity;
import az.ingress.dao.repository.StudentRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.response.PageableResponse;
import az.ingress.model.response.StudentResponse;
import az.ingress.service.BookService;
import az.ingress.service.abstraction.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static az.ingress.exception.ErrorMessage.STUDENT_NOT_FOUND;
import static az.ingress.mapper.PageableMapper.PAGEABLE_MAPPER;
import static az.ingress.mapper.StudentMapper.STUDENT_MAPPER;

@Service
public class StudentServiceHandler implements StudentService {

    private final StudentRepository studentRepository;
    private final BookService bookService;

    public StudentServiceHandler(StudentRepository studentRepository,
                                 @Lazy BookService bookService) {
        this.studentRepository = studentRepository;
        this.bookService = bookService;
    }

    @Override
    public void createStudent(CreateUserRequest userRequest) {
        var student = STUDENT_MAPPER.toStudentEntity(userRequest);
        studentRepository.save(student);
    }

    @Override
    public StudentEntity findStudentByIdIfExists(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(STUDENT_NOT_FOUND.getMessage()));
    }

    @Override
    public PageableResponse<StudentResponse> getStudentByBook(PageCriteria pageCriteria, Long bookId) {
        var pageable = PAGEABLE_MAPPER.toPageable(pageCriteria);
        var book = bookService.findBookIfExist(bookId);
        var studentsByBook = studentRepository.findStudentsByBooks(pageable, book);
        return PAGEABLE_MAPPER.toPageableResponse(studentsByBook, STUDENT_MAPPER::toStudentResponse);
    }
}
