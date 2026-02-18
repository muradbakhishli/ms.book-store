package az.ingress.service

import az.ingress.model.enums.UserRole
import az.ingress.model.request.CreateUserRequest
import az.ingress.model.request.RegistrationRequest
import az.ingress.service.abstraction.AuthorService
import az.ingress.service.abstraction.StudentService
import az.ingress.service.strategy.RegistrationStrategy
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class RegistrationStrategyTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    AuthorService authorService
    StudentService studentService
    RegistrationStrategy registrationStrategy

    def setup() {
        authorService = Mock()
        studentService = Mock()
        registrationStrategy = new RegistrationStrategy(authorService, studentService)
    }

    def "Test registrationUser success case"() {
        given:
        def registrationRequest = random.nextObject(RegistrationRequest)
        def role = registrationRequest.role.name()
        def createUserRequest = CreateUserRequest.of(registrationRequest)

        when:
        registrationStrategy.registrationStrategy(registrationRequest)

        then:
        if (role == "AUTHOR") {
            authorService.createAuthor(createUserRequest)
        } else if (role == "STUDENT") {
            studentService.createStudent(createUserRequest)
        }
    }
}
