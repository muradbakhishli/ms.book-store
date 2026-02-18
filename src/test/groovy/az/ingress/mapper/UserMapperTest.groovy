package az.ingress.mapper

import az.ingress.dao.entity.RoleEntity
import az.ingress.dao.entity.UserEntity
import az.ingress.model.request.CreateUserRequest
import az.ingress.model.request.RegistrationRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.UserMapper.USER_MAPPER

class UserMapperTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestMapRegistrationRequestToMapper"() {
        given:
        def registrationRequest = random.nextObject(RegistrationRequest)
        def roleEntity = random.nextObject(RoleEntity)

        when:
        def userEntity = USER_MAPPER.toUserEntity(registrationRequest, roleEntity)

        then:
        userEntity.username == registrationRequest.email
        userEntity.roles == Set.of(roleEntity)
    }

    def "TestMapEntityToResponse" () {
        given:
        def userEntity = random.nextObject(UserEntity)

        when:
        def userResponse = USER_MAPPER.toUserResponse(userEntity)

        then:
        userResponse.userId == userEntity.id
        userResponse.email == userEntity.username

    }
}
