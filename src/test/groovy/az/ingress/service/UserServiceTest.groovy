package az.ingress.service

import az.ingress.dao.entity.UserEntity
import az.ingress.dao.repository.UserRepository
import az.ingress.exception.AlreadyExistException
import az.ingress.exception.ErrorMessage
import az.ingress.exception.NotFoundException
import az.ingress.model.request.RegistrationRequest
import az.ingress.service.abstraction.UserService
import az.ingress.service.concrete.UserServiceHandler
import az.ingress.service.strategy.RegistrationStrategy
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    UserRepository userRepository
    PasswordEncoder passwordEncoder
    RegistrationStrategy registrationStrategy
    UserService userService

    def setup() {
        userRepository = Mock()
        passwordEncoder = Mock()
        registrationStrategy = Mock()
        userService = new UserServiceHandler(userRepository, passwordEncoder, registrationStrategy)
    }

    def "Test getUserByEmail success case"() {
        given:
        def email = random.nextObject(String)
        def userEntity = random.nextObject(UserEntity)

        when:
        def actual = userService.getUserByEmail(email)

        then:
        1 * userRepository.findByUsername(email) >> Optional.of(userEntity)
        actual.userId == userEntity.id
        actual.email == userEntity.username
    }

    def "Test getUserByEmail not found case"() {
        given:
        def email = random.nextObject(String)

        when:
        userService.getUserByEmail(email)

        then:
        1 * userRepository.findByUsername(email) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "User not found"
    }

    def "Test registrationUser method success case"() {
        given:
        def registrationRequest = random.nextObject(RegistrationRequest)

        when:
        userService.registrationUser(registrationRequest)

        then:
        1 * userRepository.existsByUsername(registrationRequest.getEmail()) >> false
        1 * userRepository.save(_ as UserEntity)
        1 * registrationStrategy.registrationStrategy(registrationRequest)
    }

    def "Test registrationUser method already exist exception case" () {
        given:
        def registrationRequest = random.nextObject(RegistrationRequest)

        when:
        userService.registrationUser(registrationRequest)

        then:
        1 * userRepository.existsByUsername(registrationRequest.getEmail()) >> true
        AlreadyExistException ex = thrown()
        ex.message == "Already user exists with this username"
    }

}
