package az.ingress.mapper

import az.ingress.dao.entity.StudentEntity
import az.ingress.model.request.CreateUserRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.StudentMapper.STUDENT_MAPPER
import static az.ingress.mapper.StudentMapper.STUDENT_MAPPER

class StudentMapperTest extends Specification{

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()


    def "TestMapRequestToEntity" () {
        given:
        def userCreateRequest = random.nextObject(CreateUserRequest)

        when:
        def studentEntity = STUDENT_MAPPER.toStudentEntity(userCreateRequest)

        then:
        studentEntity.firstName == userCreateRequest.firstName
        studentEntity.lastName == userCreateRequest.lastName
        studentEntity.age == userCreateRequest.age
    }

    def "TestMapEntityToResponse" () {
        given:
        def studentEntity = random.nextObject(StudentEntity)

        when:
        def studentResponse = STUDENT_MAPPER.toStudentResponse(studentEntity)

        then:
        studentResponse.id == studentEntity.id
        studentResponse.firstName == studentEntity.firstName
        studentResponse.lastName == studentEntity.lastName
        studentResponse.age == studentEntity.age
    }
}
