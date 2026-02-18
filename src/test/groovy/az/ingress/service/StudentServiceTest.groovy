package az.ingress.service

import az.ingress.dao.entity.StudentEntity
import az.ingress.dao.repository.StudentRepository
import az.ingress.model.request.CreateUserRequest
import az.ingress.service.abstraction.StudentService
import az.ingress.service.concrete.StudentServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class StudentServiceTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    StudentRepository studentRepository
    BookService bookService
    StudentService studentService

    def setup() {
        studentRepository = Mock()
        bookService = Mock()
        studentService = new StudentServiceHandler(studentRepository, bookService)
    }

    def "Test create student success case"() {
        given:
        def createUserRequest = random.nextObject(CreateUserRequest)

        when:
        studentService.createStudent(createUserRequest)

        then:
        1 * studentRepository.save(_ as StudentEntity)
    }
}
