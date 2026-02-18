package az.ingress.mapper

import az.ingress.model.request.CreateUserRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.AuthorMapper.AUTHOR_MAPPER

class AuthMapperTest extends Specification{

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()


    def "TestMapRequestToEntity"() {
        given:
        def createUserRequest = random.nextObject(CreateUserRequest)

        when:
        def authorEntity = AUTHOR_MAPPER.toAuthorEntity(createUserRequest)

        then:
        authorEntity.firstName == createUserRequest.firstName
        authorEntity.lastName == createUserRequest.lastName
        authorEntity.age == createUserRequest.age

    }
}
