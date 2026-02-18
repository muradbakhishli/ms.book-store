package az.ingress.controller;

import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.response.PageableResponse;
import az.ingress.model.response.StudentResponse;
import az.ingress.service.abstraction.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public PageableResponse<StudentResponse> getStudentByBook(PageCriteria pageCriteria, @RequestParam Long bookId) {
        return studentService.getStudentByBook(pageCriteria, bookId);
    }
}
