package az.ingress.service

import az.ingress.dao.entity.AuthorEntity
import az.ingress.dao.entity.StudentEntity
import az.ingress.dao.repository.AuthorRepository
import az.ingress.exception.NotFoundException
import az.ingress.mapper.AuthorMapper
import az.ingress.model.request.CreateUserRequest
import az.ingress.service.abstraction.AuthorService
import az.ingress.service.abstraction.StudentService
import az.ingress.service.concrete.AuthorServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.AuthorMapper.AUTHOR_MAPPER

class AuthorServiceTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    AuthorRepository authorRepository
    StudentService studentService
    AuthorService authorService

    def setup() {
        authorRepository = Mock()
        studentService = Mock()
        authorService = new AuthorServiceHandler(authorRepository, studentService)
    }

    def "Test CreateAuthor success case"() {
        given:
        def createUserRequest = random.nextObject(CreateUserRequest)

        when:
        authorService.createAuthor(createUserRequest)

        then:
        1 * authorRepository.save(_ as AuthorEntity)
    }

    def "Test subscribe method success case" () {
        given:
        def id = random.nextLong()
        def studentId = random.nextLong()
        def author = random.nextObject(AuthorEntity)
        def student = random.nextObject(StudentEntity)

        when:
        authorService.subscribe(id, studentId)

        then:
        1 * authorRepository.findById(id) >> Optional.of(author)
        1 * studentService.findStudentByIdIfExists(studentId) >> student

        author.students.contains(student)
        student.authors.contains(author)
    }

    def "Test subscribe method when author not found case" () {
        given:
        def id = random.nextLong()
        def studentId = random.nextLong()
        def student = random.nextObject(StudentEntity)

        when:
        authorService.subscribe(id, studentId)

        then:
        1 * authorRepository.findById(id) >> Optional.empty()
        0 * studentService.findStudentByIdIfExists(studentId) >> student

        thrown(NotFoundException)
    }
}
