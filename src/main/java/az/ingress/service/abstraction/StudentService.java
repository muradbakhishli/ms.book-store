package az.ingress.service.abstraction;

import az.ingress.dao.entity.StudentEntity;
import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.request.RegistrationRequest;
import az.ingress.model.response.PageableResponse;
import az.ingress.model.response.StudentResponse;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    void createStudent(CreateUserRequest registrationRequest);

    StudentEntity findStudentByIdIfExists(Long id);

    PageableResponse<StudentResponse> getStudentByBook(PageCriteria pageCriteria, Long bookId);
}
